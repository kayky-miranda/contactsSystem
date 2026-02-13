package com.contactsSystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<?> buildResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", status.value(),
                        "error", status.getReasonPhrase(),
                        "message", message
                ));
    }

    @ExceptionHandler(InfosJaExistenteException.class)
    public ResponseEntity<?> ExceptionDoEmail(InfosJaExistenteException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CepInvalidoException.class)
    public ResponseEntity<?> ExceptionDoCep(CepInvalidoException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataInvalidaException.class)
    public ResponseEntity<?> ExceptionDoData(DataInvalidaException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
