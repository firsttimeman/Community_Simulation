package zerobaseproject.community.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zerobaseproject.community.member.dto.MemberDTO;
import zerobaseproject.community.member.dto.RegisterDTO;
import zerobaseproject.community.member.entity.Member;
import zerobaseproject.community.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class RegisterController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerMember(@RequestBody RegisterDTO RegisterDTO) {
        MemberDTO register = memberService.signUp(RegisterDTO);

        return new ResponseEntity<>(register, HttpStatus.OK);
    }
}
