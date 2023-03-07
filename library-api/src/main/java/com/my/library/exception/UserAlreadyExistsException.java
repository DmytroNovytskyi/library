package com.my.library.exception;

public class UserAlreadyExistsException extends DataConsistencyViolationException {
    private static final String MESSAGE = "User with this username or email already exists!";

    public UserAlreadyExistsException() {
        super(MESSAGE);
    }
}
