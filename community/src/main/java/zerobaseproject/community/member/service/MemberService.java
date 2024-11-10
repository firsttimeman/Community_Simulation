package zerobaseproject.community.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobaseproject.community.global.exception.ErrorCode;
import zerobaseproject.community.global.exception.MemberException;
import zerobaseproject.community.member.type.UserRoles;
import zerobaseproject.community.member.dto.MemberDTO;
import zerobaseproject.community.member.dto.MemberInfoDTO;
import zerobaseproject.community.member.dto.RegisterDTO;
import zerobaseproject.community.member.dto.UpdateDTO;
import zerobaseproject.community.member.entity.Member;
import zerobaseproject.community.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 회원 가입
     * @param registerDTO 회원가입 요청 dto
     * @return 로그인 성공 dto
     */

    public RegisterDTO signUp(RegisterDTO registerDTO) {

        String email = registerDTO.getEmail();

                memberRepository.findByEmail(email)
                .ifPresent(member -> {
                    throw new MemberException(ErrorCode.ALREADY_EXIST_EMAIL);
                });

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



        return RegisterDTO.from(saved);

    }

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
        if (!bCryptPasswordEncoder.matches(updateDTO.getPassword(), member.getPassword())) {
            member.setPassword(bCryptPasswordEncoder.encode(updateDTO.getPassword()));
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

    }




}
