package com.my.library.controller.api;

import com.my.library.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
public interface UserApi {
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    UserDto getById(@PathVariable("id") long id);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<UserDto> getSortedPage(@RequestParam("page") int page,
                                @RequestParam("size") int size,
                                @RequestParam("sortBy") String sortBy,
                                @RequestParam("order") String order);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserDto create(@RequestBody UserDto book);

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    UserDto update(@RequestBody UserDto book);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable("id") long id);

    @PostMapping("/issue/{userId}/{bookId}")
    UserDto issueBook(@PathVariable("userId") long userId, @PathVariable("bookId") long bookId);

    @PostMapping("/return/{userId}/{bookId}")
    UserDto returnBook(@PathVariable("userId") long userId, @PathVariable("bookId") long bookId);
}
