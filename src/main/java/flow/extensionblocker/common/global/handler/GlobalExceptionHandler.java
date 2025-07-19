package flow.extensionblocker.common.global.handler;

import flow.extensionblocker.common.dto.ErrorResponse;
import flow.extensionblocker.common.global.exception.BlockerAlreadyDeletedException;
import flow.extensionblocker.common.global.exception.BlockerAlreadyExistsException;
import flow.extensionblocker.common.global.exception.BlockerLimitExceededException;
import flow.extensionblocker.common.global.exception.BlockerNotFoundException;
import flow.extensionblocker.common.global.exception.BlockerValidationException;
import flow.extensionblocker.common.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BlockerAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleBlockerAlreadyExistsException(
      BlockerAlreadyExistsException e) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(e.getCode(), e.getMessage(), null));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      MethodArgumentNotValidException e) {
    String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(
            ErrorCode.BLOCKER_VALIDATION_ERROR.getCode(),
            ErrorCode.BLOCKER_VALIDATION_ERROR.getMessage(),
            message
        ));
  }

  @ExceptionHandler(BlockerValidationException.class)
  public ResponseEntity<ErrorResponse> handleBlockerValidationException(
      BlockerValidationException e) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(e.getCode(), e.getMessage(), null));
  }

  @ExceptionHandler(BlockerLimitExceededException.class)
  public ResponseEntity<ErrorResponse> handleBlockerLimitExceededException(
      BlockerLimitExceededException e) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(e.getCode(), e.getMessage(), null));
  }

  @ExceptionHandler(BlockerNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleBlockerNotFoundException(
      BlockerNotFoundException e) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse(e.getCode(), e.getMessage(), null));
  }

  @ExceptionHandler(BlockerAlreadyDeletedException.class)
  public ResponseEntity<ErrorResponse> handleBlockerAlreadyDeletedException(
      BlockerAlreadyDeletedException e) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(e.getCode(), e.getMessage(), null));
  }
}
