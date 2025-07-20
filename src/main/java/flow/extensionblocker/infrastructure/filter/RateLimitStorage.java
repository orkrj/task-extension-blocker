package flow.extensionblocker.infrastructure.filter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RateLimitStorage {

  private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
  private final Map<String, Long> fixedWindow = new ConcurrentHashMap<>();

  public boolean isAllowed(String clientKey, int limit, long windowSize) {
    long now = System.currentTimeMillis();

    fixedWindow.compute(clientKey, (key, startTime) -> {
      if (startTime == null || now - startTime >= windowSize) {
        requestCounts.put(key, new AtomicInteger(1));
        return now;
      }

      return startTime;
    });

    Long currentWindowStart = fixedWindow.get(clientKey);
    if (currentWindowStart != null && (now - currentWindowStart) < windowSize) {
      AtomicInteger counter = requestCounts.get(clientKey);
      if (counter != null) {
        int currentCount = counter.get();
        if (currentCount < limit) {
          return counter.incrementAndGet() <= limit;
        }

        return false;
      }
    }

    return true;
  }

  @Scheduled(fixedRate = 300_000)
  public void clean() {
    long now = System.currentTimeMillis();
    long expireTime = 10 * 60 * 1000;

    fixedWindow.entrySet().removeIf(entry -> {
      if (now - entry.getValue() > expireTime) {
        requestCounts.remove(entry.getKey());
        return true;
      }

      return false;
    });
  }
}
