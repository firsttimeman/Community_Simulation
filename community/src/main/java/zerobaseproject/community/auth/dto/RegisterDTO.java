package zerobaseproject.community.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import zerobaseproject.community.member.entity.Member;
import zerobaseproject.community.member.type.UserRoles;

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

    @NotNull(message = "사용자 역할을 설정해 주세요")
    private UserRoles userRoles;

    private String address;

    private String phoneNumber;

    public static RegisterDTO from(Member member) {
        return RegisterDTO.builder()
                .email(member.getEmail())
                .name(member.getName())
                .password(member.getPassword())
                .address(member.getAddress())
                .phoneNumber(member.getPhoneNumber())
                .userRoles(member.getUserRoles())
                .build();
    }
}
