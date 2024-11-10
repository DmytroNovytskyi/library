package com.my.library.exception;

/**
 * Custom exception class for a scenario where a book already exists for an author and book name.
 */
public class BookAlreadyExistsException extends DataConsistencyViolationException {

    /**
     * Custom message for BookAlreadyExistsException.
     */
    private static final String MESSAGE = "Book for author with this name already exists!";

    /**
     * Constructor for BookAlreadyExistsException.
     */
    public BookAlreadyExistsException() {
        super(MESSAGE);
    }
}
