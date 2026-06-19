package com.examen.pedidos.exception;

import com.examen.pedidos.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PedidoNotFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handlePedidoNotFound(PedidoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.builder()
                        .codigo(404)
                        .mensaje(ex.getMessage())
                        .objeto(null)
                        .build());
    }

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<BaseResponse<Object>> handleStockInsuficiente(StockInsuficienteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(BaseResponse.builder()
                        .codigo(409)
                        .mensaje(ex.getMessage())
                        .objeto(null)
                        .build());
    }

    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handleClienteNotFound(ClienteNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.builder()
                        .codigo(404)
                        .mensaje(ex.getMessage())
                        .objeto(null)
                        .build());
    }

    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handleProductoNotFound(ProductoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.builder()
                        .codigo(404)
                        .mensaje(ex.getMessage())
                        .objeto(null)
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errores = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.builder()
                        .codigo(400)
                        .mensaje(errores)
                        .objeto(null)
                        .build());
    }
}
