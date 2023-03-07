package com.my.library.exception;

public class BookAlreadyExistsException extends DataConsistencyViolationException {
    private static final String MESSAGE = "Book for author with this name already exists!";

    public BookAlreadyExistsException() {
        super(MESSAGE);
    }
}
