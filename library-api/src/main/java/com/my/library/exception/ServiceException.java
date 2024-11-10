package com.my.library.exception;

import com.my.library.exception.wrapper.ExceptionType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Abstract class for custom exceptions related to service failures.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class ServiceException extends RuntimeException {
    /**
     * Exception type.
     */
    private ExceptionType exceptionType;

    /**
     * Constructor for ServiceException.
     *
     * @param message       the exception message with problem description
     * @param exceptionType the type of the exception
     */
    public ServiceException(String message, ExceptionType exceptionType) {
        super(message);
        this.exceptionType = exceptionType;
    }
}
