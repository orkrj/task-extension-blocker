package flow.extensionblocker.presentation.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import flow.extensionblocker.application.BlockerService;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPart;

@ExtendWith(MockitoExtension.class)
class ExtensionBlockerFilterTest {

  @Mock
  private BlockerService blockerService;

  @Mock
  private MockFilterChain chain;

  @InjectMocks
  private ExtensionBlockerFilter sut;

  @Test
  @DisplayName("차단된 확장자에 대한 upload 요청은 415 에러를 반환한다")
  void test1() throws ServletException, IOException {
    // Given
    var request = new MockHttpServletRequest();
    request.setMethod("POST");
    request.setServletPath("/uploads");
    request.addPart(new MockPart("file", "test.exe", new byte[0]));

    var response = new MockHttpServletResponse();

    given(blockerService.isBlocked("exe")).willReturn(true);

    // When
    sut.doFilterInternal(request, response, chain);

    // Then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
  }

  @Test
  @DisplayName("차단되지 않은 확장자에 대한 upload 요청은 정상적으로 처리된다")
  void test2() throws ServletException, IOException {
    // Given
    var request = new MockHttpServletRequest();
    request.setMethod("POST");
    request.setServletPath("/uploads");
    request.addPart(new MockPart("file", "test.txt", new byte[0]));

    var response = new MockHttpServletResponse();

    given(blockerService.isBlocked("txt")).willReturn(false);

    // When
    sut.doFilterInternal(request, response, chain);

    // Then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
  }

  @Test
  @DisplayName("upload 요청이 아닌 경우 필터가 동작하지 않는다")
  void test3() throws ServletException, IOException {
    // Given
    var request = new MockHttpServletRequest();
    request.setMethod("GET");
    request.setServletPath("/blockers");

    var response = new MockHttpServletResponse();

    // When
    sut.doFilter(request, response, chain);

    // Then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
  }
}