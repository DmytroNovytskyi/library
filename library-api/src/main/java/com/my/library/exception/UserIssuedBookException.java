package com.my.library.exception;

public class UserIssuedBookException extends DataConsistencyViolationException {
    private static final String MESSAGE = "User has issued book!";

    public UserIssuedBookException() {
        super(MESSAGE);
    }
}
