package com.my.library.exception;

/**
 * Custom exception class for a scenario where a book already issued to a user.
 */
public class BookAlreadyIssuedException extends DataConsistencyViolationException {

    /**
     * Custom message for BookAlreadyIssuedException.
     */
    private static final String MESSAGE = "Book had been already issued to user!";

    /**
     * Constructor for BookAlreadyIssuedException.
     */
    public BookAlreadyIssuedException() {
        super(MESSAGE);
    }
}
