package flow.extensionblocker.infrastructure.filter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RateLimitStorageTest {

  private RateLimitStorage sut;

  @BeforeEach
  void setUp() {
    sut = new RateLimitStorage();
  }

  @Test
  @DisplayName("첫 번째 요청은 항상 허용한다")
  void test1() {
    // When
    boolean result = sut.isAllowed("client1", 5, 60_000L);

    // Then
    assertTrue(result);
  }

  @Test
  @DisplayName("허용 한도 내에서는 모든 요청을 허용한다")
  void test2() {
    // Given
    String clientKey = "client1";
    int limit = 3;
    long windowSize = 60_000L;

    // When & Then
    for (int i = 0; i < limit; i++) {
      boolean result = sut.isAllowed(clientKey, limit, windowSize);
      assertTrue(result, "요청: " + (i + 1) + " / " + limit);
    }
  }

  @Test
  @DisplayName("허용 한도를 초과하면 요청을 거부한다")
  void test3() {
    // Given
    String clientKey = "client1";
    int limit = 2;
    long windowSize = 60_000L;

    for (int i = 0; i < limit; i++) {
      sut.isAllowed(clientKey, limit, windowSize);
    }

    // When
    boolean result = sut.isAllowed(clientKey, limit, windowSize);

    // Then
    assertFalse(result);
  }

  @Test
  @DisplayName("윈도우 시간이 만료되면 카운트가 리셋된다")
  void test4() throws InterruptedException {
    // Given
    String clientKey = "client1";
    int maxRequests = 2;
    long windowMs = 100L; // 100ms 윈도우

    sut.isAllowed(clientKey, maxRequests, windowMs);
    sut.isAllowed(clientKey, maxRequests, windowMs);

    Thread.sleep(200L);

    // When
    boolean result = sut.isAllowed(clientKey, maxRequests, windowMs);

    // Then
    assertTrue(result);
  }

  @Test
  @DisplayName("서로 다른 클라이언트는 독립적으로 카운트된다")
  void test5() {
    // Given
    String client1 = "client1";
    String client2 = "client2";
    int limit = 2;
    long windowSize = 60_000L;

    sut.isAllowed(client1, limit, windowSize);
    sut.isAllowed(client1, limit, windowSize);

    // When
    boolean result = sut.isAllowed(client2, limit, windowSize);

    // Then
    assertTrue(result);
  }
}