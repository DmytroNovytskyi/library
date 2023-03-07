package com.my.library.exception.handler;

import com.my.library.exception.ServiceException;
import com.my.library.exception.wrapper.ExceptionDetails;
import com.my.library.exception.wrapper.ExceptionType;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ExceptionDetails> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getAllErrors().stream()
                .map(e -> new ExceptionDetails(e.getDefaultMessage(),
                        ExceptionType.VALIDATION_EXCEPTION,
                        LocalDateTime.now()))
                .toList();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ExceptionDetails> handleConstraintViolationException(ConstraintViolationException ex) {
        return ex.getConstraintViolations().stream()
                .map(e -> new ExceptionDetails(e.getMessage(),
                        ExceptionType.VALIDATION_EXCEPTION,
                        LocalDateTime.now()))
                .toList();
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDetails handleServiceException(ServiceException ex, HandlerMethod hm) {
        return new ExceptionDetails(ex.getMessage(), ex.getExceptionType(), LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDetails handleException(Exception ex, HandlerMethod hm) {
        return new ExceptionDetails(ex.getMessage(), ExceptionType.UNEXPECTED_EXCEPTION, LocalDateTime.now());
    }
}
