package com.my.library.exception;

public class BookNotFoundException extends NotFoundException {
    private static final String MESSAGE = "Book was not found!";

    public BookNotFoundException() {
        super(MESSAGE);
    }
}
