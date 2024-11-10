package com.my.library.controller;

import com.my.library.controller.api.BookApi;
import com.my.library.dto.BookDto;
import com.my.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * RestController class for handling requests related to books.
 */
@RestController
@RequiredArgsConstructor
public class BookController implements BookApi {
    /**
     * The BookService object that will be used to manage books.
     */
    private final BookService bookService;

    /**
     * Returns a book with the given ID.
     *
     * @param id the ID of the book to retrieve
     * @return the book with the given ID
     */
    @Override
    public BookDto getById(long id) {
        return bookService.getById(id);
    }

    /**
     * Retrieves a sorted and paginated list of books.
     *
     * @param page   the page number of the results to retrieve
     * @param size   the maximum number of results per page
     * @param sortBy the field to sort the results by
     * @param order  the sort order
     * @return a page of books sorted and filtered according to the specified parameters
     */
    @Override
    public Page<BookDto> getSortedPage(int page, int size, String sortBy, String order) {
        return bookService.getSortedPage(page, size, sortBy, order);
    }

    /**
     * Creates a new book.
     *
     * @param book the book to create
     * @return the newly created book
     */
    @Override
    public BookDto create(BookDto book) {
        return bookService.create(book);
    }

    /**
     * Updates an existing book.
     *
     * @param book the book to update
     * @return the updated book
     */
    @Override
    public BookDto update(BookDto book) {
        return bookService.update(book);
    }

    /**
     * Deletes the Book entity with the specified ID.
     *
     * @param id the ID of the book to delete
     * @return a ResponseEntity with no content
     */
    @Override
    public ResponseEntity<Void> delete(long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
