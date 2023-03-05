package com.my.library.service;

import com.my.library.dto.UserDto;
import org.springframework.data.domain.Page;

public interface UserService {
    UserDto getById(long id);

    Page<UserDto> getSortedPage(int page, int size, String sortBy, String order);

    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto);

    void deleteById(long id);

    UserDto issueBook(long userId, long bookId);

    UserDto returnBook(long userId, long bookId);
}
