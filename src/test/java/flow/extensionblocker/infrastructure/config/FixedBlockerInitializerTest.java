package flow.extensionblocker.infrastructure.config;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

import flow.extensionblocker.domain.Blocker;
import flow.extensionblocker.domain.BlockerRepository;
import flow.extensionblocker.domain.Type;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FixedBlockerInitializerTest {

  @Mock
  private BlockerRepository blockerRepository;

  @InjectMocks
  private FixedBlockerInitializer sut;

  @Test
  @DisplayName("저장되어 있지 않은 고정 확장자만 비활성화 상태로 미리 생성한다.")
  void test1() throws Exception {
    // Given
    var extensions = sut.FIXED_EXTENSIONS;
    extensions.forEach(extension -> {
      given(blockerRepository.findBlocker(extension)).willReturn(Optional.empty());
    });

    // When
    sut.run(null);

    // Then
    extensions.forEach(extension -> {
      verify(blockerRepository, times(1))
          .createBlocker(
              argThat(e ->
                  e.getType() == Type.FIXED &&
                  e.getExtension().equals(extension)
              )
          );
    });
  }

  @Test
  @DisplayName("이미 저장되어 있는 고정 확장자는 생성하지 않는다.")
  void test2() throws Exception {
    // Given
    var extensions = sut.FIXED_EXTENSIONS;
    extensions.forEach(extension -> {
      given(blockerRepository.findBlocker(extension))
          .willReturn(Optional.of(Blocker.of(extension, Type.FIXED, false)));
    });

    // When
    sut.run(null);

    // Then
    verify(blockerRepository, never()).createBlocker(any());
  }
}