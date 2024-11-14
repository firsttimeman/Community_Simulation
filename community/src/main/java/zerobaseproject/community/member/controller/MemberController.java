package zerobaseproject.community.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import zerobaseproject.community.member.dto.MemberDTO;
import zerobaseproject.community.member.dto.MemberInfoDTO;
import zerobaseproject.community.auth.dto.RegisterDTO;
import zerobaseproject.community.member.dto.UpdateDTO;
import zerobaseproject.community.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member-info/{email}")
    @PreAuthorize("#email == authentication.principal.username or hasRole('ADMIN')")
    public ResponseEntity<MemberDTO> getMemberInfo(@PathVariable String email) {
        MemberDTO responseDTO = memberService.getMemberInfo(email);

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/update/{email}")
    @PreAuthorize("#email == authentication.principal.username")
    public ResponseEntity<UpdateDTO> updateMember(@PathVariable String email
            , @RequestBody UpdateDTO updateDTO) {
        UpdateDTO updateMember = memberService.updateMemberInfo(email, updateDTO);
        return new ResponseEntity<>(updateMember, HttpStatus.OK);

    }

    @GetMapping("/findAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<MemberInfoDTO>> getAllMembers(Pageable pageable) {
        Page<MemberInfoDTO> members = memberService.getAllMembers(pageable);
        return new ResponseEntity<>(members, HttpStatus.OK);
    }


    @DeleteMapping("/delete/{email}")
    @PreAuthorize("#email == authentication.principal.username or hasRole('ADMIN')")
    public ResponseEntity<?> deleteMember(@PathVariable String email) {
        memberService.deleteMember(email);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }





}
