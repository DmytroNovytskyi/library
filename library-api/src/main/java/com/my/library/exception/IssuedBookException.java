package com.my.library.exception;

/**
 * Custom exception class for a scenario where a book is issued to a user and being deleted.
 */
public class IssuedBookException extends DataConsistencyViolationException {

    /**
     * Custom message for IssuedBookException.
     */
    private static final String MESSAGE = "Book is issued by user!";

    /**
     * Constructor for IssuedBookException.
     */
    public IssuedBookException() {
        super(MESSAGE);
    }
}
