package ru.mentee.power.crm.spring.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    Map<String, String> errors = new HashMap<>();
    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
    fieldErrors.forEach(
        (fieldError) -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
    ErrorResponse errorResponse =
        new ErrorResponse(
            LocalDateTime.now(),
            status.value(),
            "400 Bad Request",
            "Неверный формат данных.",
            request.getDescription(false).substring(4),
            errors);
    return ResponseEntity.badRequest().body(errorResponse); // заменить
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFound(
      EntityNotFoundException ex, WebRequest request) {
    ErrorResponse errorResponse =
        new ErrorResponse(
            LocalDateTime.now(),
            404,
            "Not Found",
            ex.getMessage(),
            request.getDescription(false).substring(4));
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse); // заменить
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
    log.info("Unexpected error", ex);
    ErrorResponse errorResponse =
        new ErrorResponse(
            LocalDateTime.now(),
            500,
            null,
            "Internal server error occurred. Contact support.",
            request.getDescription(false).substring(4));
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse); // заменить
  }
}
