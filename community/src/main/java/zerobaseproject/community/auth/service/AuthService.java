package zerobaseproject.community.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zerobaseproject.community.auth.dto.LoginDTO;
import zerobaseproject.community.member.entity.Member;
import zerobaseproject.community.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public void authenticateUser(LoginDTO loginDTO) {

        Member member = memberRepository.findByEmail(loginDTO.getEmail());
        if (member == null) {
            throw new RuntimeException("이메일이 존재하지 않습니다.");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), member.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

    }
}
