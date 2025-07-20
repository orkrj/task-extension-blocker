package flow.extensionblocker.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BlockerViewController {

  @GetMapping
  public String getBlockerView() {
    return "index";
  }
}
