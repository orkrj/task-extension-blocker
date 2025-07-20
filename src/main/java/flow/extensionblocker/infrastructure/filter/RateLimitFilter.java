package flow.extensionblocker.infrastructure.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

  private final RateLimitStorage storage;

  private static final int RATE_LIMIT = 60;
  private static final String WHITE_LIST_IP = "127.0.0.1";

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {

    String clientIp = getClientIp(request);
    if (clientIp.equals(WHITE_LIST_IP)) {
      chain.doFilter(request, response);
      return;
    }

    String clientKey = this.generateClientKey(request, clientIp);
    boolean isAllowed = storage.isAllowed(clientKey, RATE_LIMIT, 60_000L);

    if (!isAllowed) {
      log.warn("어뷰저: {}, IP: {}, uri: {}", clientKey, clientIp, request.getRequestURI());
      this.handleRateLimitExceeded(response);
      return;
    }

    chain.doFilter(request, response);
  }

  private String getClientIp(HttpServletRequest request) {
    String xForwardedFor = request.getHeader("X-Forwarded-For");
    if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
      return xForwardedFor.split(",")[0].trim();
    }

    String xRealIp = request.getHeader("X-Real-IP");
    if (xRealIp != null && !xRealIp.isEmpty()) {return xRealIp;}
    return request.getRemoteAddr();
  }

  private String generateClientKey(HttpServletRequest request, String clientIp) {
    String userAgent = request.getHeader("User-Agent");

    if (userAgent != null && !userAgent.isEmpty()) {return clientIp + ":" + userAgent.hashCode();}
    return clientIp;
  }

  private void handleRateLimitExceeded(HttpServletResponse response) throws IOException {
    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    response.setContentType("application/json;charset=UTF-8");
    response.setHeader("Retry-After", "600");
    response.getWriter().write("너무 많은 요청이 발생했습니다. 잠시 후 다시 시도해주세요.");
    response.getWriter().flush();
    response.getWriter().close();
  }
}
