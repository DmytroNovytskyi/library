package com.my.library.exception;

import com.my.library.exception.wrapper.ExceptionType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class ServiceException extends RuntimeException {
    ExceptionType exceptionType;

    public ServiceException(String message, ExceptionType exceptionType) {
        super(message);
        this.exceptionType = exceptionType;
    }
}
