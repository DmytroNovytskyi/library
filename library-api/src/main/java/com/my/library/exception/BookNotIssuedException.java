package com.my.library.exception;

/**
 * Custom exception class for a scenario where a book is not issued to a user.
 */
public class BookNotIssuedException extends DataConsistencyViolationException {

    /**
     * Custom message for BookNotIssuedException.
     */
    private static final String MESSAGE = "Book was not issued to user!";

    /**
     * Constructor for BookNotIssuedException.
     */
    public BookNotIssuedException() {
        super(MESSAGE);
    }
}
