package com.my.library.exception;

/**
 * Custom exception class for a scenario where a user has issued books and being deleted.
 */
public class UserIssuedBookException extends DataConsistencyViolationException {

    /**
     * Custom message for UserIssuedBookException.
     */
    private static final String MESSAGE = "User has issued book!";

    /**
     * Constructor for UserIssuedBookException.
     */
    public UserIssuedBookException() {
        super(MESSAGE);
    }
}
