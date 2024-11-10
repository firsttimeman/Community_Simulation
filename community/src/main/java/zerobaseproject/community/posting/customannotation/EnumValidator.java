package zerobaseproject.community.posting.customannotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValueValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValidator {
    Class<? extends Enum<?>> enumClass(); // 검사할 Enum 클래스
    String message() default "유효하지 않은 값입니다."; // 기본 메시지
    Class<?>[] groups() default {}; // 검증 그룹
    Class<? extends Payload>[] payload() default {}; // 메타정보
}
