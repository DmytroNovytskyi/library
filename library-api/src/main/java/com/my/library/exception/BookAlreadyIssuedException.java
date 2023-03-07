package com.my.library.exception;

public class BookAlreadyIssuedException extends DataConsistencyViolationException {
    private static final String MESSAGE = "Book had been already issued to user!";

    public BookAlreadyIssuedException() {
        super(MESSAGE);
    }
}
