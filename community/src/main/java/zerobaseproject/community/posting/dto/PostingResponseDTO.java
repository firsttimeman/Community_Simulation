package zerobaseproject.community.posting.dto;

import lombok.*;
import zerobaseproject.community.member.entity.Member;
import zerobaseproject.community.posting.entity.Posting;
import zerobaseproject.community.posting.type.Category;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostingResponseDTO {

    private Long postingId;
    private String title;
    private String content;
    private Category category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String email;

    public static PostingResponseDTO fromEntity(Posting posting) {
        return PostingResponseDTO.builder()
                .postingId(posting.getPostingId())
                .email(posting.getMember().getEmail())
                .title(posting.getTitle())
                .content(posting.getContent())
                .category(posting.getCategory())
                .createdAt(posting.getCreateDate())
                .updatedAt(posting.getUpdateDate())
                .build();
    }


}
