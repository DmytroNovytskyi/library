package com.my.library.exception;

public class IssuedBookException extends DataConsistencyViolationException {
    private static final String MESSAGE = "Book is issued by user!";

    public IssuedBookException() {
        super(MESSAGE);
    }
}
