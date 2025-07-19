package flow.extensionblocker.domain;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BlockerTest {

  @Nested
  @DisplayName("차단기 도메인 검증 테스트")
  class BlockerValidationTest {

    private final Validator validator = buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("1자에 대한 확장자 차단기를 생성할 수 있다")
    void test_success_1() {
      // Given
      String input = "a";

      // When
      var blocker = Blocker.of(input, Type.CUSTOM);
      var violations = validator.validate(blocker);

      // Then
      assertThat(violations).isEmpty();
      assertThat(blocker.getExtension()).isEqualTo("a");
    }

    @Test
    @DisplayName("20자에 대한 확장자 차단기를 생성할 수 있다")
    void test_success_2() {
      // Given
      String input = "abcdefghijklmopqrstu";

      // When
      var blocker = Blocker.of(input, Type.CUSTOM);
      var violations = validator.validate(blocker);

      // Then
      assertThat(violations).isEmpty();
      assertThat(blocker.getExtension()).isEqualTo("abcdefghijklmopqrstu");
    }

    @Test
    @DisplayName("빈 확장자에 대한 차단기를 생성할 수 없다")
    void test_fail_1() {
      // Given
      String input = "";

      // When
      var blocker = Blocker.of(input, Type.CUSTOM);
      var violations = validator.validate(blocker);

      // Then
      assertThat(violations)
          .extracting(ConstraintViolation::getMessage)
          .containsExactly("확장자는 1자 이상 20자 이하의 영문 대소문자와 숫자로만 구성되어야 합니다.");
    }

    @Test
    @DisplayName("20자를 초과한 확장자에 대한 차단기를 생성할 수 없다")
    void test_fail_2() {
      // Given
      String input = "abcdefghijklmopqrstuv";

      // When
      var blocker = Blocker.of(input, Type.CUSTOM);
      var violations = validator.validate(blocker);

      // Then
      assertThat(violations)
          .extracting(ConstraintViolation::getMessage)
          .containsExactly("확장자는 1자 이상 20자 이하의 영문 대소문자와 숫자로만 구성되어야 합니다.");
    }

    @Test
    @DisplayName("특수 문자를 포함한 확장자에 대한 차단기를 생성할 수 없다")
    void test_fail_3() {
      // Given
      String input = "abcdef*";

      // When
      var blocker = Blocker.of(input, Type.CUSTOM);
      var violations = validator.validate(blocker);

      // Then
      assertThat(violations)
          .extracting(ConstraintViolation::getMessage)
          .containsExactly("확장자는 1자 이상 20자 이하의 영문 대소문자와 숫자로만 구성되어야 합니다.");
    }

    @Test
    @DisplayName("공백을 포함한 확장자에 대한 차단기를 생성할 수 없다")
    void test_fail_4() {
      // Given
      String input = "abc def";

      // When
      var blocker = Blocker.of(input, Type.CUSTOM);
      var violations = validator.validate(blocker);

      // Then
      assertThat(violations)
          .extracting(ConstraintViolation::getMessage)
          .containsExactly("확장자는 1자 이상 20자 이하의 영문 대소문자와 숫자로만 구성되어야 합니다.");
    }
  }
}