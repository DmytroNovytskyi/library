package com.my.library.controller.api;

import com.my.library.dto.UserDto;
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
 * This interface defines REST API for managing users.
 */
@Validated
@RequestMapping("/user")
public interface UserApi {

    /**
     * Retrieves a user with the specified ID.
     *
     * @param id the ID of the user to retrieve
     * @return the user with the specified ID
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    UserDto getById(@Validated @PathVariable("id") @Min(value = 1, message = "{userApi.getById.id.min}") long id);

    /**
     * Retrieves a sorted and paginated list of users
     *
     * @param page   the page number (0-based) of the results to retrieve, "0" by default
     * @param size   the maximum number (1-based) of results per page, "1" by default
     * @param sortBy the field to sort the results by (id, username, or email), "id" by default
     * @param order  the sort order (asc or desc), "asc" by default
     * @return a page of users sorted and filtered according to the specified parameters
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<UserDto> getSortedPage(@RequestParam(value = "page", defaultValue = "0")
                                @Min(value = 0, message = "{userApi.getSortedPage.page.min}") int page,
                                @RequestParam(value = "size", defaultValue = "1")
                                @Min(value = 1, message = "{userApi.getSortedPage.size.min}") int size,
                                @RequestParam(value = "sortBy", defaultValue = "id")
                                @Pattern(regexp = "id|username|email",
                                        message = "{userApi.getSortedPage.sortBy.pattern}") String sortBy,
                                @RequestParam(value = "order", defaultValue = "asc")
                                @Pattern(regexp = "asc|desc",
                                        message = "{userApi.getSortedPage.order.pattern}") String order);

    /**
     * Creates a new user
     *
     * @param user the user to create
     * @return the newly created user
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserDto create(@RequestBody @Validated(OnCreate.class) UserDto user);

    /**
     * Updates an existing user.
     *
     * @param user the user to update
     * @return the updated user.
     */
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    UserDto update(@RequestBody @Validated(OnUpdate.class) UserDto user);

    /**
     * Deletes a user with the specified ID.
     *
     * @param id the ID of the user to delete
     * @return a ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable("id")
                                @Min(value = 1, message = "{userApi.delete.id.min}") long id);

    /**
     * Issues a book to a user.
     *
     * @param userId the ID of the user to issue the book to
     * @param bookId the ID of the book to issue
     * @return the updated user
     */
    @PostMapping("/issue/{userId}/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    UserDto issueBook(@PathVariable("userId")
                      @Min(value = 1, message = "{userApi.issueBook.userId.min}") long userId,
                      @PathVariable("bookId")
                      @Min(value = 1, message = "{userApi.issueBook.bookId.min}") long bookId);

    /**
     * Returns a book from a user.
     *
     * @param userId the ID of the user to return the book from
     * @param bookId the ID of the book to return
     * @return the updated user
     */
    @PostMapping("/return/{userId}/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    UserDto returnBook(@PathVariable("userId")
                       @Min(value = 1, message = "{userApi.returnBook.userId.min}") long userId,
                       @PathVariable("bookId")
                       @Min(value = 1, message = "{userApi.returnBook.bookId.min}") long bookId);
}
