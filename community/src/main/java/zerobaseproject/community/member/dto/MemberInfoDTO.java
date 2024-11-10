package zerobaseproject.community.member.dto;

import lombok.Builder;
import lombok.Data;
import zerobaseproject.community.member.type.UserRoles;
import zerobaseproject.community.member.entity.Member;

@Data
@Builder
public class MemberInfoDTO {

    private String email;
    private String name;
    private String phoneNumber;
    private String address;
    private UserRoles userRoles;

    public static MemberInfoDTO from(Member member) {
        return MemberInfoDTO.builder()
                .email(member.getEmail())
                .name(member.getName())
                .address(member.getAddress())
                .phoneNumber(member.getPhoneNumber())
                .userRoles(member.getUserRoles())
                .build();
    }
}
