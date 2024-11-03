package zerobaseproject.community.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import zerobaseproject.community.global.type.UserRoles;
import zerobaseproject.community.member.entity.Member;

import javax.management.relation.Role;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {

    private Long userId;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String address;
    private UserRoles userRoles;

    public static MemberDTO fromEntity(Member member) {
        return MemberDTO.builder()
                .userId(member.getUserId())
                .password(member.getPassword())
                .email(member.getEmail())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .address(member.getAddress())
                .userRoles(member.getUserRoles())
                .build();
    }


}
