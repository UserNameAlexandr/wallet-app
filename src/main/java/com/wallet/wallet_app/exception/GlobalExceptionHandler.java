package com.wallet.wallet_app.exception;

import com.wallet.wallet_app.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {WalletNotFoundException.class})
    public ResponseEntity<ErrorDTO> handleWalletNotFoundException (WalletNotFoundException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value={InsufficientFundsException.class})
    public ResponseEntity<ErrorDTO> handleInsufficientFunds(InsufficientFundsException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage(), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(errorDTO, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorDTO> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = "Invalid JSON format";
        ErrorDTO errorDTO = new ErrorDTO(message + ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder("Validation failed: ");
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append("; "));
        ErrorDTO errorDTO = new ErrorDTO(errors.toString(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGeneral(Exception ex) {
        ErrorDTO errorDTO = new ErrorDTO("Internal server error" + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


