package zerobaseproject.community.auth.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    private String email;
    private String password;
}
