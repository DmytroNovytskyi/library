package com.my.library.controller;

import com.my.library.controller.api.UserApi;
import com.my.library.dto.UserDto;
import com.my.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * RestController class for handling requests related to users.
 */
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    /**
     * The UserService object that will be used to manage users.
     */
    private final UserService userService;

    /**
     * Retrieves a user with the specified ID.
     *
     * @param id the ID of the user to retrieve
     * @return the user with the specified ID
     */
    @Override
    public UserDto getById(long id) {
        return userService.getById(id);
    }

    /**
     * Retrieves a sorted and paginated list of users
     *
     * @param page   the page number of the results to retrieve
     * @param size   the maximum number of results per page
     * @param sortBy the field to sort the results by
     * @param order  the sort order
     * @return a page of users sorted and filtered according to the specified parameters
     */
    @Override
    public Page<UserDto> getSortedPage(int page, int size, String sortBy, String order) {
        return userService.getSortedPage(page, size, sortBy, order);
    }

    /**
     * Creates a new user
     *
     * @param user the user to create
     * @return the newly created user
     */
    @Override
    public UserDto create(UserDto user) {
        return userService.create(user);
    }

    /**
     * Updates an existing user.
     *
     * @param user the user to update
     * @return the updated user.
     */
    @Override
    public UserDto update(UserDto user) {
        return userService.update(user);
    }

    /**
     * Deletes a user with the specified ID.
     *
     * @param id the ID of the user to delete
     * @return a ResponseEntity with no content
     */
    @Override
    public ResponseEntity<Void> delete(long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Issues a book to a user.
     *
     * @param userId the ID of the user to issue the book to
     * @param bookId the ID of the book to issue
     * @return the updated user
     */
    @Override
    public UserDto issueBook(long userId, long bookId) {
        return userService.issueBook(userId, bookId);
    }

    /**
     * Returns a book from a user.
     *
     * @param userId the ID of the user to return the book from
     * @param bookId the ID of the book to return
     * @return the updated user
     */
    @Override
    public UserDto returnBook(long userId, long bookId) {
        return userService.returnBook(userId, bookId);
    }
}
