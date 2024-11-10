package com.my.library.exception;

/**
 * Custom exception class for a scenario where a book cannot be found.
 */
public class BookNotFoundException extends NotFoundException {

    /**
     * Custom message for BookNotFoundException.
     */
    private static final String MESSAGE = "Book was not found!";

    /**
     * Constructor for BookNotFoundException.
     */
    public BookNotFoundException() {
        super(MESSAGE);
    }
}
