package com.my.library.exception;

import com.my.library.exception.wrapper.ExceptionType;

public abstract class DataConsistencyViolationException extends ServiceException {
    public DataConsistencyViolationException(String message) {
        super(message, ExceptionType.DATABASE_EXCEPTION);
    }
}
