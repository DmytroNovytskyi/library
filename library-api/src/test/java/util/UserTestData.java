package util;

import com.my.library.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

public class UserTestData {
    public static final Long ID = 1L;
    public static final Long INVALID_ID = 0L;
    public static final String USERNAME = "test_user";
    public static final String EMAIL = "test_email@gmail.com";
    public static final String PASSWORD = "testpassword1!";

    public static UserDto createUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(ID);
        userDto.setUsername(USERNAME);
        userDto.setEmail(EMAIL);
        return userDto;
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
