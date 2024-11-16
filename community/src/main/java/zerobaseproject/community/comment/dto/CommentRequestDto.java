package zerobaseproject.community.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDto {



    @NotBlank(message = "댓글 내용을 입력해주세요.")
    @Size(max = 100, message = "댓글은 최대 100자까지 입력할 수 있습니다.")
    private String content;

    @NotNull
    private String Email;

    @NotNull
    private Long postingId;
}
