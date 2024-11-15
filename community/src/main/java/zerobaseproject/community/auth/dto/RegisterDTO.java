package zerobaseproject.community.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Size(max = 50, message = "이름은 최대 50자까지 입력할 수 있습니다.")
    private String name;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min = 8, max = 20, message = "비밀번호는 8자에서 20자 사이여야 합니다")
    private String password;

    @NotNull(message = "사용자 역할을 설정해 주세요")
    private UserRoles userRoles;

    @Size(max = 100, message = "주소는 최대 100자까지 입력할 수 있습니다.")
    private String address;

    @Pattern(regexp = "^\\d{10,15}$", message = "전화번호는 10~15자리의 숫자여야 합니다.")
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
