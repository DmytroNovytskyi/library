package com.my.library.exception;

public class NoAvailableBooksException extends DataConsistencyViolationException {
    private static final String MESSAGE = "No available books!";

    public NoAvailableBooksException() {
        super(MESSAGE);
    }
}
