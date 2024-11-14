package zerobaseproject.community.posting.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import zerobaseproject.community.posting.type.Category;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostingRequestDTO {

        @NotNull
        private String title;

        @NotNull
        private String content;

        @NotNull(message = "카테고리를 선택해 주세요.")
        private Category category;


}
