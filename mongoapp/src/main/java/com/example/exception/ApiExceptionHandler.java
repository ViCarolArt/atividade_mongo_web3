package com.example.mongoapp.exception;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> tratarValidacao(
            MethodArgumentNotValidException ex) {

        String mensagem = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(erro -> erro.getField() + ": "
                    + erro.getDefaultMessage())
            .collect(Collectors.joining("; "));

        return ResponseEntity
            .badRequest()
            .body(Map.of("erro", mensagem));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> tratarStatus(
            ResponseStatusException ex) {

        HttpStatus status = HttpStatus.valueOf(
            ex.getStatusCode().value()
        );

        return ResponseEntity
            .status(status)
            .body(Map.of(
                "erro",
                "Não foi possível concluir a operação."
            ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> tratarErroGeral(
            Exception ex) {

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of(
                "erro",
                "Ocorreu um erro interno. Tente novamente."
            ));
    }
}