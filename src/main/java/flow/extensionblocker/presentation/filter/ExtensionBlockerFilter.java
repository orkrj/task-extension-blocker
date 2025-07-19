package flow.extensionblocker.presentation.filter;

import flow.extensionblocker.application.BlockerService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class ExtensionBlockerFilter extends OncePerRequestFilter {

  private final BlockerService blockerService;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getServletPath();
    String method = request.getMethod();

    return !"POST".equals(method) && !path.startsWith("/uploads");
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {
    Part part = request.getPart("file");
    String filename = part.getSubmittedFileName();
    String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();

    if (blockerService.isBlocked(extension)) {
      response.sendError(
          HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), // 415
          extension + " 확장자는 차단되었습니다."
      );

      return;
    }

    filterChain.doFilter(request, response);
  }
}
