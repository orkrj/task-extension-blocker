package flow.extensionblocker.infrastructure.config;

import flow.extensionblocker.domain.Blocker;
import flow.extensionblocker.domain.BlockerRepository;
import flow.extensionblocker.domain.Type;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FixedBlockerInitializer implements ApplicationRunner {

  private final BlockerRepository blockerRepository;

  public static final List<String> FIXED_EXTENSIONS = List.of(
      "bat", "cmd", "com", "cpl", "exe", "scr", "js"
  );

  @Override
  @Transactional
  public void run(ApplicationArguments args) throws Exception {
    FIXED_EXTENSIONS.stream()
        .filter(extension -> blockerRepository
            .findBlocker((extension))
            .isEmpty()
        )
        .map(left -> Blocker.of(left, Type.FIXED, false))
        .forEach(blockerRepository::createBlocker);
  }
}
