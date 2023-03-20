package com.my.library.exception;

/**
 * Custom exception class for a scenario where a user already exists for username or email.
 */
public class UserAlreadyExistsException extends DataConsistencyViolationException {

    /**
     * Custom message for UserAlreadyExistsException.
     */
    private static final String MESSAGE = "User with this username or email already exists!";

    /**
     * Constructor for UserAlreadyExistsException.
     */
    public UserAlreadyExistsException() {
        super(MESSAGE);
    }
}
