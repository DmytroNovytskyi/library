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

@Validated
@RequestMapping("/user")
public interface UserApi {
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    UserDto getById(@Validated @PathVariable("id") @Min(value = 1, message = "{userApi.getById.id.min}") long id);

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserDto create(@RequestBody @Validated(OnCreate.class) UserDto book);

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    UserDto update(@RequestBody @Validated(OnUpdate.class) UserDto book);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable("id")
                                @Min(value = 1, message = "{userApi.delete.id.min}") long id);

    @PostMapping("/issue/{userId}/{bookId}")
    UserDto issueBook(@PathVariable("userId")
                      @Min(value = 1, message = "{userApi.issueBook.userId.min}") long userId,
                      @PathVariable("bookId")
                      @Min(value = 1, message = "{userApi.issueBook.bookId.min}") long bookId);

    @PostMapping("/return/{userId}/{bookId}")
    UserDto returnBook(@PathVariable("userId")
                       @Min(value = 1, message = "{userApi.returnBook.userId.min}") long userId,
                       @PathVariable("bookId")
                       @Min(value = 1, message = "{userApi.returnBook.bookId.min}") long bookId);
}
