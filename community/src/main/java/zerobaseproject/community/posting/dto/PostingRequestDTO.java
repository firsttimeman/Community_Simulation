package zerobaseproject.community.posting.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import zerobaseproject.community.posting.customannotation.EnumValidator;
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

        @EnumValidator(enumClass = Category.class, message = "유효하지 않은 카테고리입니다.")
        private Category category;


}
