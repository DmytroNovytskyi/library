package com.my.library.exception;

import com.my.library.exception.wrapper.ExceptionType;

public abstract class NotFoundException extends ServiceException {
    public NotFoundException(String message) {
        super(message, ExceptionType.PROCESSING_EXCEPTION);
    }
}
