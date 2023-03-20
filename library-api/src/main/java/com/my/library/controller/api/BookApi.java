package com.my.library.controller.api;

import com.my.library.dto.BookDto;
import com.my.library.validation.group.OnCreate;
import com.my.library.validation.group.OnUpdate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * This interface defines REST API for managing books.
 */
@Validated
@RequestMapping("/book")
public interface BookApi {

    /**
     * Returns a book with the given ID.
     *
     * @param id the ID of the book to retrieve
     * @return the book with the given ID
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    BookDto getById(@PathVariable("id") @Min(value = 1, message = "{bookApi.getById.id.min}") long id);

    /**
     * Retrieves a sorted and paginated list of books.
     *
     * @param page   the page number (0-based) of the results to retrieve, "0" by default
     * @param size   the maximum number (1-based) of results per page, "1" by default
     * @param sortBy the field to sort the results by (id, author, or name), "id" by default
     * @param order  the sort order (asc or desc), "asc" by default
     * @return a page of books sorted and filtered according to the specified parameters
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<BookDto> getSortedPage(@RequestParam(value = "page", defaultValue = "0")
                                @Min(value = 0, message = "{bookApi.getSortedPage.page.min}") int page,
                                @RequestParam(value = "size", defaultValue = "1")
                                @Min(value = 1, message = "{bookApi.getSortedPage.size.min}") int size,
                                @RequestParam(value = "sortBy", defaultValue = "id")
                                @Pattern(regexp = "id|author|name",
                                        message = "{bookApi.getSortedPage.sortBy.pattern}") String sortBy,
                                @RequestParam(value = "order", defaultValue = "asc")
                                @Pattern(regexp = "asc|desc",
                                        message = "{bookApi.getSortedPage.order.pattern}") String order);

    /**
     * Creates a new book.
     *
     * @param book the book to create
     * @return the newly created book
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    BookDto create(@RequestBody @Validated(OnCreate.class) BookDto book);

    /**
     * Updates an existing book.
     *
     * @param book the book to update
     * @return the updated book
     */
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    BookDto update(@RequestBody @Validated(OnUpdate.class) BookDto book);

    /**
     * Deletes the Book entity with the specified ID.
     *
     * @param id the ID of the book to delete
     * @return a ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable("id")
                                @Min(value = 1, message = "{bookApi.delete.id.min}") long id);
}
