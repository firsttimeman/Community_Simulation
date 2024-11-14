package zerobaseproject.community.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobaseproject.community.global.exception.ErrorCode;
import zerobaseproject.community.global.exception.MemberException;
import zerobaseproject.community.member.type.UserRoles;
import zerobaseproject.community.member.dto.MemberDTO;
import zerobaseproject.community.member.dto.MemberInfoDTO;
import zerobaseproject.community.auth.dto.RegisterDTO;
import zerobaseproject.community.member.dto.UpdateDTO;
import zerobaseproject.community.member.entity.Member;
import zerobaseproject.community.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;


    /**
     * 회원 개인 정보 조회
     * @param email 이메일로 회원정보 조회
     * @return 회원 정보 반환 dto
     */

    public MemberDTO getMemberInfo(String email) {
      Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(ErrorCode.USER_NOT_FOUND));

      return MemberDTO.fromEntity(member);
    }

    /**
     * 회원 정보 수정
     * @param email
     * @param updateDTO
     * @return
     */

    @Transactional
    public UpdateDTO updateMemberInfo(String email, UpdateDTO updateDTO) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(ErrorCode.USER_NOT_FOUND));


        member.setAddress(updateDTO.getAddress());
        member.setName(updateDTO.getName());
        member.setPhoneNumber(updateDTO.getPhoneNumber());
        if (!passwordEncoder.matches(updateDTO.getPassword(), member.getPassword())) {
            member.setPassword(passwordEncoder.encode(updateDTO.getPassword()));
        }

        log.info("멤버 정보수정 성공");
        return UpdateDTO.from(member);
    }

    /**
     * 전체 회원 조회
     * @param pageable
     * @return
     */

    public Page<MemberInfoDTO> getAllMembers(Pageable pageable) {
        return memberRepository.findAll(pageable)
                .map(MemberInfoDTO::from);
    }


    /**
     * 회원 탈퇴
     * @param email
     */
    @Transactional
    public void deleteMember(String email) {
        memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(ErrorCode.USER_NOT_FOUND));
        memberRepository.deleteByEmail(email);
        log.info("계정 삭제완료");
        String redisKey = "RT:" + email;
        redisTemplate.delete(redisKey);
        log.info("Redis에서 Refresh Token 삭제 완료: {}", redisKey);

    }




}
