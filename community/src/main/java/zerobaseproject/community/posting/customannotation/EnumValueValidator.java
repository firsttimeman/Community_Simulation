package zerobaseproject.community.posting.customannotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValueValidator implements ConstraintValidator<EnumValidator, String> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(EnumValidator constraintAnnotation) {
      this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        if(value == null) {
            return true;
        }

        return Arrays.stream(enumClass.getEnumConstants()) // Enum 값 중 일치하는 값이 있는지 확인
                .anyMatch(e -> e.name().equals(value));
    }
}
