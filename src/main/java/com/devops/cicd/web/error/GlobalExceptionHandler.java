package com.devops.cicd.web.error;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationExceptions(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    return ResponseEntity.badRequest()
        .body(
            createErrorResponse(
                400,
                "Validation Error",
                ex.getBindingResult().getFieldError().getDefaultMessage(),
                request.getRequestURI()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Object> handleIllegalArgument(
      IllegalArgumentException ex, HttpServletRequest request) {
    return ResponseEntity.badRequest()
        .body(createErrorResponse(400, "Bad Request", ex.getMessage(), request.getRequestURI()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleAllUncaughtException(
      Exception ex, HttpServletRequest request) {
    return ResponseEntity.internalServerError()
        .body(
            createErrorResponse(
                500,
                "Internal Server Error",
                "Une erreur inattendue s'est produite",
                request.getRequestURI()));
  }

  private Map<String, Object> createErrorResponse(
      int status, String error, String message, String path) {
    Map<String, Object> errorDetails = new HashMap<>();
    errorDetails.put("timestamp", Instant.now().toString());
    errorDetails.put("status", status);
    errorDetails.put("error", error);
    errorDetails.put("message", message);
    errorDetails.put("path", path);
    return errorDetails;
  }
}
