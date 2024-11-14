package zerobaseproject.community.auth.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zerobaseproject.community.global.exception.ErrorCode;
import zerobaseproject.community.global.exception.MemberException;
import zerobaseproject.community.global.exception.TokenException;
import zerobaseproject.community.member.dto.MemberDTO;
import zerobaseproject.community.auth.dto.RegisterDTO;
import zerobaseproject.community.auth.dto.SignInDto;
import zerobaseproject.community.member.entity.Member;
import zerobaseproject.community.member.repository.MemberRepository;
import zerobaseproject.community.auth.jwt.JwtProvider;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, String> redisTemplate;

    // 회원가입 메서드 추가
    public MemberDTO signUp(RegisterDTO registerDTO) {
        // 이미 존재하는 회원인지 확인
        if (memberRepository.existsByEmail(registerDTO.getEmail())) {
           throw new MemberException(ErrorCode.ALREADY_EXIST_EMAIL);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());

        // 새로운 회원 엔터티 생성 및 저장
        Member newMember = Member.builder()
                .email(registerDTO.getEmail())
                .name(registerDTO.getName())
                .password(encodedPassword)
                .address(registerDTO.getAddress())
                .phoneNumber(registerDTO.getPhoneNumber())
                .userRoles(registerDTO.getUserRoles())
                .build();

        Member savedMember = memberRepository.save(newMember);

        return MemberDTO.fromEntity(savedMember);
    }

    public SignInDto.Response signIn(SignInDto.Request request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new MemberException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new MemberException(ErrorCode.PASSWORD_MISMATCH);
        }

        String accessToken = jwtProvider.createAccessToken(member.getEmail(), member.getUserRoles());
        String refreshToken = jwtProvider.createRefreshToken(member.getEmail());

        // Redis에 Refresh Token 저장 (7일 만료)
        redisTemplate.opsForValue().set("RT:" + member.getEmail(), refreshToken, 7, TimeUnit.DAYS);

        return SignInDto.Response.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String reissueToken(String refreshToken) {
        String email = jwtProvider.getUserEmail(refreshToken);
        String storedRefreshToken = redisTemplate.opsForValue().get("RT:" + email);

        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new TokenException(ErrorCode.INVALID_OR_EXPIRED_REFRESH_TOKEN);
        }

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(ErrorCode.USER_NOT_FOUND));
        return jwtProvider.createAccessToken(member.getEmail(), member.getUserRoles());
    }

    public void signOut(String email, String accessToken) {
        // Refresh Token 삭제
        String redisKey = "RT:" + email;
        redisTemplate.delete(redisKey);
        log.info("레디스 키 삭제: {}", email);

        // Access Token 블랙리스트로 추가
        long expiration = jwtProvider.getExpiration(accessToken); // 만료 시간 가져오기
        String blackListKey = "BL:" + email + ":" + accessToken;
        redisTemplate.opsForValue().set(blackListKey, "logged-out", expiration, TimeUnit.MILLISECONDS);
        log.info("블랙리스트에 등록된 Access Token (사용자: {}): {}", email, accessToken);
    }

}
