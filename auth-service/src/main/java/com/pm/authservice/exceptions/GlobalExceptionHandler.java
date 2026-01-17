package com.pm.authservice.exceptions;

import com.pm.authservice.exceptions.UserAlreadyExistsException;
import com.pm.patientservice.dto.ApiErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleValidationException(MethodArgumentNotValidException ex) {
        var fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        ApiErrorResponseDTO error = ApiErrorResponseDTO
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation Failed")
                .errors(fieldErrors)
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        var error = ApiErrorResponseDTO
                .builder()
                .status(HttpStatus.CONFLICT.value())
                .message("User Already Exists")
                .build();

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ BadCredentialsException.class, UsernameNotFoundException.class })
    public ResponseEntity<ApiErrorResponseDTO> handleAuthExceptions(RuntimeException e) {
        ApiErrorResponseDTO error = ApiErrorResponseDTO.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Invalid email or password")
                .build();

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ UserNotFoundException.class })
    public ResponseEntity<ApiErrorResponseDTO> handleUserNotFoundException(UserNotFoundException ex) {
        ApiErrorResponseDTO error = ApiErrorResponseDTO.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message("User not found")
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
