package flow.extensionblocker.common.validation;

import flow.extensionblocker.common.annotation.ValidExtension;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ExtensionValidator implements ConstraintValidator<ValidExtension, String> {

  private static final java.util.regex.Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9]{1,20}$");

  @Override
  public boolean isValid(String extension, ConstraintValidatorContext constraintValidatorContext) {
    if (extension == null || extension.isBlank()) {
      return false;
    }

    return PATTERN.matcher(extension).matches();
  }
}
