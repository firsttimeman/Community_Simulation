package zerobaseproject.community.member.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import zerobaseproject.community.member.entity.Member;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    @Email(message = "유효한 이메일을 입력해주세요")
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;

    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    private String address;

    private String phoneNumber;


    public static RegisterDTO from(Member member) {

        return RegisterDTO.builder()
                .email(member.getEmail())
                .name(member.getName())
                .password(member.getPassword()) // TODO 추후 제거할것
                .address(member.getAddress())
                .phoneNumber(member.getPhoneNumber())
                .build();
    }
}
