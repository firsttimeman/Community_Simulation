package zerobaseproject.community.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobaseproject.community.member.dto.MemberDTO;
import zerobaseproject.community.member.dto.MemberInfoDTO;
import zerobaseproject.community.member.dto.RegisterDTO;
import zerobaseproject.community.member.dto.UpdateDTO;
import zerobaseproject.community.member.service.MemberService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<RegisterDTO> registerMember(@RequestBody RegisterDTO RegisterDTO) {
        RegisterDTO responseDTO = memberService.signUp(RegisterDTO);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/member-info/{email}")
    public ResponseEntity<MemberDTO> getMemberInfo(@PathVariable String email) {
        MemberDTO responseDTO = memberService.getMemberInfo(email);

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/update/{email}")
    public ResponseEntity<UpdateDTO> updateMember(@PathVariable String email
            , @RequestBody UpdateDTO updateDTO) {
        UpdateDTO updateMember = memberService.updateMemberInfo(email, updateDTO);
        return new ResponseEntity<>(updateMember, HttpStatus.OK);

    }

    @GetMapping("/findAll")
    public ResponseEntity<Page<MemberInfoDTO>> getAllMembers(Pageable pageable) {
        Page<MemberInfoDTO> members = memberService.getAllMembers(pageable);
        return new ResponseEntity<>(members, HttpStatus.OK);
    }


    @DeleteMapping("/delete/{email}")
    public ResponseEntity<?> deleteMember(@PathVariable String email) {
        memberService.deleteMember(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }





}
