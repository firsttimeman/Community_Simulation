package zerobaseproject.community.member.dto;


import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class RegisterDTO {

    private String email;
    private String name;
    private String password;
    private String address;
    private String phoneNumber;

    public static RegisterDTO from(MemberDTO memberDTO) {

        return RegisterDTO.builder()
                .email(memberDTO.getEmail())
                .name(memberDTO.getName())
                .password(memberDTO.getPassword())
                .address(memberDTO.getAddress())
                .phoneNumber(memberDTO.getPhoneNumber())
                .build();
    }
}
