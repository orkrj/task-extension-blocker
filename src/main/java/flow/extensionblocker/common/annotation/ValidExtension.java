package flow.extensionblocker.common.annotation;

import flow.extensionblocker.common.validation.ExtensionValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExtensionValidator.class)
public @interface ValidExtension {
  String message() default "확장자는 1자 이상 20자 이하의 영문 대소문자와 숫자로만 구성되어야 합니다.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
