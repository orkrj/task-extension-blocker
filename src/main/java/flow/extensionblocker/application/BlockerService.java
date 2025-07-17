package flow.extensionblocker.application;

import flow.extensionblocker.application.dto.CreateBlockerRequest;
import flow.extensionblocker.domain.Blocker;
import flow.extensionblocker.domain.BlockerRepository;
import flow.extensionblocker.application.dto.CreateBlockerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlockerService {

  private final BlockerRepository blockerRepository;

  public CreateBlockerResponse createBlocker(CreateBlockerRequest request) {
    Blocker blocker = blockerRepository.createBlocker(CreateBlockerRequest.toBlocker(request));
    return CreateBlockerResponse.from(blocker);
  }
}
