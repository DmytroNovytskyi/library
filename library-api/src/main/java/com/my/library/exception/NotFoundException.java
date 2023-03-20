package com.my.library.exception;

import com.my.library.exception.wrapper.ExceptionType;

/**
 * Abstract class for custom exceptions related to resource not found scenarios.
 */
public abstract class NotFoundException extends ServiceException {
    /**
     * Constructor for NotFoundException.
     *
     * @param message the exception message with problem description
     */
    public NotFoundException(String message) {
        super(message, ExceptionType.PROCESSING_EXCEPTION);
    }
}
