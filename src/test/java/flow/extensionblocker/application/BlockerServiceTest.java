package flow.extensionblocker.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import flow.extensionblocker.application.dto.CreateBlockerRequest;
import flow.extensionblocker.common.global.exception.blocker.BlockerAlreadyExistsException;
import flow.extensionblocker.common.global.exception.blocker.BlockerLimitExceededException;
import flow.extensionblocker.domain.Blocker;
import flow.extensionblocker.domain.BlockerRepository;
import flow.extensionblocker.domain.Type;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BlockerServiceTest {

  @Mock
  private BlockerRepository blockerRepository;

  @InjectMocks
  private BlockerService sut;

  @Nested
  @DisplayName("차단기 생성 테스트")
  class CreateBlocker {

    @BeforeEach
    void setUp() {
      given(blockerRepository.createBlocker(any()))
          .willAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    @DisplayName("하나의 확장자에 대한 차단기를 생성한다")
    void test1() {
      // Given
      CreateBlockerRequest input = new CreateBlockerRequest("exe");

      // When
      var response = sut.createBlocker(input);

      // Then
      assertThat(response.extension()).isEqualTo("exe");
    }

    @Test
    @DisplayName("입력값이 대문자로 주어진 경우에도 소문자로 변환하여 차단기를 생성한다")
    void test2() {
      // Given
      CreateBlockerRequest input = new CreateBlockerRequest("EXE");

      // When
      var response = sut.createBlocker(input);

      // Then
      assertThat(response.extension()).isEqualTo("exe");
    }
  }

  @Nested
  @DisplayName("차단기 생성 시나리오 테스트")
  class CreateBlockerScenario {

    @Test
    @DisplayName("이미 존재하는 차단기를 생성할 수 없다")
    void test1() {
      // Given
      CreateBlockerRequest input = new CreateBlockerRequest("exe");
      Blocker blocker = Blocker.of("exe");

      given(blockerRepository.countCustomBlockers()).willReturn(100);
      given(blockerRepository.findBlocker("exe")).willReturn(Optional.of(blocker));

      // When & Then
      assertThatThrownBy(() -> sut.createBlocker(input))
          .isInstanceOf(BlockerAlreadyExistsException.class)
          .hasMessage("이미 존재하는 차단기입니다.");
    }

    @Test
    @DisplayName("논리적으로 삭제된 차단기를 다시 생성할 수 있다.")
    void test2() {
      // Given
      Blocker blocker = Blocker.of("exe");
      blocker.delete();

      given(blockerRepository.countCustomBlockers()).willReturn(100);
      given(blockerRepository.findBlocker(any())).willReturn(Optional.of(blocker));

      CreateBlockerRequest input = new CreateBlockerRequest("exe");

      // When
      var response = sut.createBlocker(input);

      // Then
      assertThat(response.extension()).isEqualTo("exe");
    }

    @Test
    @DisplayName("커스텀 차단기가 199개인 경우 차단기를 생성할 수 있다")
    void test3() {
      // Given
      CreateBlockerRequest input = new CreateBlockerRequest("exe");
      Blocker blocker = Blocker.of("exe");

      given(blockerRepository.countCustomBlockers()).willReturn(199);
      given(blockerRepository.findBlocker("exe")).willReturn(Optional.empty());
      given(blockerRepository.createBlocker(any())).willReturn(blocker);

      // When
      var response = sut.createBlocker(input);

      // Then
      assertThat(response.extension()).isEqualTo("exe");
      assertThat(response.type()).isEqualTo(Type.CUSTOM);
    }

    @Test
    @DisplayName("커스텀 차단기가 200개를 초과할 경우 예외가 발생한다")
    void test4() {
      // Given
      given(blockerRepository.countCustomBlockers()).willReturn(200);

      // When & Then
      assertThatThrownBy(() -> sut.createBlocker(new CreateBlockerRequest("exe")))
          .isInstanceOf(BlockerLimitExceededException.class)
          .hasMessage("차단기 개수가 제한을 초과했습니다.");
    }
  }

  @Nested
  @DisplayName("차단기 삭제 테스트")
  class DeleteBlocker {

    Blocker blocker;

    @BeforeEach
    void setUp() {
      blocker = Blocker.of("exe");
      given(blockerRepository.findBlocker(any()))
          .willReturn(Optional.of(blocker));
    }

    @Test
    @DisplayName("차단기를 논리적으로 삭제한다")
    void test1() {
      // When
      sut.deleteBlocker("exe");

      // Then
      assertThat(blocker.getDeletedAt()).isNotNull();
      assertThat(blocker.getExtension()).isEqualTo("exe");
    }
  }
}
