package zerobaseproject.community.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignOutRequestDto {

    @Email(message = "유효한 이메일을 입력해주세요")
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;
}
