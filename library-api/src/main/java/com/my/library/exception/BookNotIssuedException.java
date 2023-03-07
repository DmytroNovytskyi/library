package com.my.library.exception;

public class BookNotIssuedException extends DataConsistencyViolationException {
    private static final String MESSAGE = "Book was not issued to user!";

    public BookNotIssuedException() {
        super(MESSAGE);
    }
}
