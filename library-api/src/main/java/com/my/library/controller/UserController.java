package com.my.library.controller;

import com.my.library.controller.api.UserApi;
import com.my.library.dto.UserDto;
import com.my.library.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDto getById(long id) {
        return userService.getById(id);
    }

    @Override
    public Page<UserDto> getSortedPage(int page, int size, String sortBy, String order) {
        return userService.getSortedPage(page, size, sortBy, order);
    }

    @Override
    public UserDto create(UserDto book) {
        return userService.create(book);
    }

    @Override
    public UserDto update(UserDto book) {
        return userService.update(book);
    }

    @Override
    public ResponseEntity<Void> delete(long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public UserDto issueBook(long userId, long bookId) {
        return userService.issueBook(userId, bookId);
    }

    @Override
    public UserDto returnBook(long userId, long bookId) {
        return userService.returnBook(userId, bookId);
    }
}
