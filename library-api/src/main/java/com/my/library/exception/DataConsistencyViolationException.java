package com.my.library.exception;

import com.my.library.exception.wrapper.ExceptionType;

/**
 * Abstract class for custom exceptions related to database data consistency violations.
 */
public abstract class DataConsistencyViolationException extends ServiceException {

    /**
     * Constructor for DataConsistencyViolationException.
     *
     * @param message the exception message with problem description
     */
    public DataConsistencyViolationException(String message) {
        super(message, ExceptionType.DATABASE_EXCEPTION);
    }
}
