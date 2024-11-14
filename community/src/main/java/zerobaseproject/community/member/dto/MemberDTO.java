package zerobaseproject.community.member.dto;

import lombok.*;
import zerobaseproject.community.member.type.UserRoles;
import zerobaseproject.community.member.entity.Member;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {

    private Long userId; // TODO 추후 제거할것
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String address;
    private UserRoles userRoles;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public static MemberDTO fromEntity(Member member) {
        return MemberDTO.builder()
                .userId(member.getMemberId()) // TODO 추후 제거할것
                .password(member.getPassword())
                .email(member.getEmail())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .address(member.getAddress())
                .userRoles(member.getUserRoles())
                .createDate(member.getCreateDate())
                .updateDate(member.getUpdateDate())
                .build();
    }


}
