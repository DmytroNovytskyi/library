package com.my.library.exception;

/**
 * Custom exception class for a scenario where a book is being issued but no available copies left.
 */
public class NoAvailableBooksException extends DataConsistencyViolationException {

    /**
     * Custom message for IssuedBookException.
     */
    private static final String MESSAGE = "No available books!";

    /**
     * Constructor for NoAvailableBooksException.
     */
    public NoAvailableBooksException() {
        super(MESSAGE);
    }
}
