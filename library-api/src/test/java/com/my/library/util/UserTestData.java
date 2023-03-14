package com.my.library.util;

import com.my.library.dto.UserDto;
import com.my.library.model.Book;
import com.my.library.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserTestData {
    public static final Long ID = 1L;
    public static final Long INVALID_ID = 0L;
    public static final String USERNAME = "test_user";
    public static final String EMAIL = "test_email@gmail.com";
    public static final String PASSWORD = "testpassword1!";
    public static final Set<Book> BOOKS = new HashSet<>(List.of(new Book()));

    public static User createUser() {
        User user = new User();
        user.setId(ID);
        user.setUsername(USERNAME);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        return user;
    }

    public static UserDto createUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(ID);
        userDto.setUsername(USERNAME);
        userDto.setEmail(EMAIL);
        return userDto;
    }

    public static List<User> createUserList() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setId(ID + i);
            user.setUsername(USERNAME + "a".repeat(i));
            user.setEmail("a".repeat(i) + EMAIL);
            userList.add(user);
        }
        return userList;
    }

    public static List<UserDto> createUserDtoList() {
        List<UserDto> userDtoList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            UserDto userDto = new UserDto();
            userDto.setId(ID + i);
            userDto.setUsername(USERNAME + "a".repeat(i));
            userDto.setEmail("a".repeat(i) + EMAIL);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }
}
