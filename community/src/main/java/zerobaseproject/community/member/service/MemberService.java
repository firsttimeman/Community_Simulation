package zerobaseproject.community.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import zerobaseproject.community.global.type.UserRoles;
import zerobaseproject.community.member.dto.MemberDTO;
import zerobaseproject.community.member.dto.RegisterDTO;
import zerobaseproject.community.member.entity.Member;
import zerobaseproject.community.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public MemberDTO signUp(RegisterDTO registerDTO) {

        String email = registerDTO.getEmail();

        if(memberRepository.existsByEmail(email)) {
            throw new RuntimeException("이미 존재하는 이메일 입니다");
        }



        Member saved = memberRepository.save(
                Member.builder()
                        .email(registerDTO.getEmail())
                        .name(registerDTO.getName())
                        .password(bCryptPasswordEncoder.encode(registerDTO.getPassword()))
                        .phoneNumber(registerDTO.getPhoneNumber())
                        .address(registerDTO.getAddress())
                        .userRoles(UserRoles.USER)
                        .build()
        );

        memberRepository.save(saved);

        return MemberDTO.fromEntity(saved);

    }



}
