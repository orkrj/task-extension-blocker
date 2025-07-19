package flow.extensionblocker.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import flow.extensionblocker.application.dto.CreateBlockerRequest;
import flow.extensionblocker.domain.Blocker;
import flow.extensionblocker.domain.BlockerRepository;
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

  @Nested
  @DisplayName("차단기 생성 테스트")
  class CreateBlocker {

    @Mock
    private BlockerRepository blockerRepository;

    @InjectMocks
    private BlockerService sut;

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
  @DisplayName("차단기 삭제 테스트")
  class DeleteBlocker {

    @Mock
    private BlockerRepository blockerRepository;

    @InjectMocks
    private BlockerService sut;

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
