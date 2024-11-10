package zerobaseproject.community.member.dto;

import lombok.Builder;
import lombok.Data;
import zerobaseproject.community.member.entity.Member;

import java.time.LocalDateTime;

@Data
@Builder
public class UpdateDTO {

    private String name;
    private String password;
    private String address;
    private String phoneNumber;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public static UpdateDTO from(Member member) {

        return UpdateDTO.builder()
                .name(member.getName())
                .password(member.getPassword())
                .address(member.getAddress())
                .phoneNumber(member.getPhoneNumber())
                .createDate(member.getCreateDate())
                .updateDate(member.getUpdateDate())
                .build();
    }
}
