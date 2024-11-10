package com.my.library.service;

import com.my.library.dto.BookDto;
import org.springframework.data.domain.Page;

/**
 * Service interface for managing books.
 */
public interface BookService {

    /**
     * Returns a book with the given ID.
     *
     * @param id the ID of the book to retrieve
     * @return the book with the given ID
     */
    BookDto getById(long id);

    /**
     * Retrieves a sorted and paginated list of books.
     *
     * @param page   the page number of the results to retrieve
     * @param size   the maximum number of results per page
     * @param sortBy the field to sort the results by
     * @param order  the sort order
     * @return a page of books sorted and filtered according to the specified parameters
     */
    Page<BookDto> getSortedPage(int page, int size, String sortBy, String order);

    /**
     * Creates a new book.
     *
     * @param bookDto the book to create
     * @return the newly created book
     */
    BookDto create(BookDto bookDto);

    /**
     * Updates an existing book.
     *
     * @param bookDto the book to update
     * @return the updated book
     */
    BookDto update(BookDto bookDto);

    /**
     * Deletes the Book entity with the specified ID.
     *
     * @param id the ID of the book to delete
     */
    void deleteById(long id);
}
