package com.skillswap.web;
import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*; import java.util.Map;
@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String,String>> validation(MethodArgumentNotValidException e){
    String msg=e.getBindingResult().getFieldErrors().stream().findFirst().map(f->f.getDefaultMessage()).orElse("Invalid request.");
    return ResponseEntity.badRequest().body(Map.of("message",msg)); }
}
