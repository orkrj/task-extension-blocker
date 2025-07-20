package flow.extensionblocker.infrastructure.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.anyInt;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.never;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class RateLimitFilterTest {

  @Mock
  private RateLimitStorage storage;

  @Mock
  private FilterChain chain;

  @InjectMocks
  private RateLimitFilter sut;

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  @BeforeEach
  void setUp() {
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
  }

  @Nested
  @DisplayName("RateLimit 허용 테스트")
  class RateLimitAllowedTest {

    @Test
    @DisplayName("레이트 리미트 허용 범위 내에서는 요청을 통과시킨다")
    void test1() throws Exception {
      // given
      request.setRequestURI("/blockers");
      request.setMethod("GET");
      request.setRemoteAddr("192.168.1.1");
      request.addHeader("User-Agent", "Mozilla/5.0");

      given(storage.isAllowed(anyString(), anyInt(), anyLong())).willReturn(true);

      // when
      sut.doFilter(request, response, chain);

      // then
      verify(chain).doFilter(request, response);
      verify(storage).isAllowed(anyString(), anyInt(), anyLong());
    }

    @Test
    @DisplayName("화이트리스트 IP 는 레이트 리미트를 적용하지 않는다")
    void test2() throws Exception {
      // given
      request.setRequestURI("/blockers");
      request.setMethod("GET");
      request.setRemoteAddr("127.0.0.1");

      // when
      sut.doFilter(request, response, chain);

      // then
      verify(chain).doFilter(request, response);
      verify(storage, never()).isAllowed(anyString(), anyInt(), anyLong());
    }
  }

  @Nested
  @DisplayName("RateLimit 초과 테스트")
  class RateLimitExceededTest {

    @Test
    @DisplayName("레이트 리미트 초과시 429 에러를 반환한다")
    void test1() throws Exception {
      // given
      request.setRequestURI("/blockers");
      request.setMethod("GET");
      request.setRemoteAddr("192.168.1.1");
      request.addHeader("User-Agent", "Mozilla/5.0");

      given(storage.isAllowed(anyString(), anyInt(), anyLong())).willReturn(false);

      // when
      sut.doFilter(request, response, chain);

      // then
      verify(chain, never()).doFilter(request, response);
      verify(storage).isAllowed(anyString(), anyInt(), anyLong());

      assertEquals(429, response.getStatus());
      assertEquals("application/json;charset=UTF-8", response.getContentType());
      assertEquals("600", response.getHeader("Retry-After"));
    }
  }
}