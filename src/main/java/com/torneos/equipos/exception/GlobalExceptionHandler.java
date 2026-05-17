package com.torneos.equipos.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex){
        Map<String, String> errores = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach((FieldError error)-> errores.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRunTimeException(RuntimeException ex){
        Map<String, String> error = new LinkedHashMap<>();
        error.put("error", ex.getMessage());

        return ResponseEntity.badRequest().body(error);
    }
    @ExceptionHandler(feign.FeignException.class)
    public ResponseEntity<?> handleFeignException(feign.FeignException ex) {

        String respuestaOriginal = ex.contentUTF8();
        if (respuestaOriginal != null && !respuestaOriginal.isEmpty()) {
            return ResponseEntity.status(ex.status())
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(respuestaOriginal);
        }
        if (ex.status() == 404) {
            Map<String, String> error = new java.util.LinkedHashMap<>();
            error.put("error", "Recurso no encontrado");
            error.put("mensaje", "El usuario que intenta buscar, asignar o ejecutar la acción no existe o se encuentra inactivo.");
            return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND).body(error);
        }
        Map<String, String> error = new java.util.LinkedHashMap<>();
        error.put("error", "Error en servicio externo");
        error.put("mensaje", "Ocurrió un problema de comunicación. " + ex.status());
        return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
