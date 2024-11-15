package zerobaseproject.community.comment.dto;


import lombok.*;
import zerobaseproject.community.comment.entity.Comment;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {

    private Long commentId;
    private String content;
    private String email;
    private Long postingId;

    public static CommentResponseDto toResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .email(comment.getMember().getEmail())
                .postingId(comment.getPosting().getPostingId())
                .build();
    }

}
