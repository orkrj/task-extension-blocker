package flow.extensionblocker.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/uploads")
public class UploadController {

  @PostMapping
  public ResponseEntity<HttpStatus> upload(@RequestBody MultipartFile file) {
    // 실질적으로 파일 저장은 하지 않음
    return ResponseEntity.status(HttpStatus.CREATED).body(HttpStatus.CREATED); // 201
  }
}
