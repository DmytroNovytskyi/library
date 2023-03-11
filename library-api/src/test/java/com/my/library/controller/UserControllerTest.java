package com.my.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.library.dto.UserDto;
import com.my.library.exception.wrapper.ExceptionType;
import com.my.library.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.my.library.util.CommonTestData.*;
import static com.my.library.util.UserTestData.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void givenValidUserId_whenGetById_thenReturnUserDto() throws Exception {
        when(userService.getById(ID)).thenReturn(createUserDto());

        mockMvc.perform(get("/user/{id}", ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.books").doesNotExist());
        verify(userService).getById(ID);
    }

    @Test
    void givenInvalidUserId_whenGetById_thenReturnValidationExceptionListJson() throws Exception {
        mockMvc.perform(get("/user/{id}", INVALID_ID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].exceptionType").value(ExceptionType.VALIDATION_EXCEPTION.name()));
        verify(userService, never()).getById(INVALID_ID);
    }

    @Test
    void givenValidPageableData_whenGetSortedPage_thenReturnPageOfUsers() throws Exception {
        List<UserDto> userDtoList = createUserDtoList();
        Page<UserDto> userDtoPage = createPage(userDtoList);
        when(userService.getSortedPage(PAGE, SIZE, SORT_BY, ORDER)).thenReturn(userDtoPage);

        mockMvc.perform(get("/user")
                        .queryParam("page", String.valueOf(PAGE))
                        .queryParam("size", String.valueOf(SIZE))
                        .queryParam("sortBy", SORT_BY)
                        .queryParam("order", ORDER))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.pageable.pageNumber").value(PAGE))
                .andExpect(jsonPath("$.pageable.pageSize").value(SIZE))
                .andExpect(jsonPath("$.pageable.sort.sorted").value(true))
                .andExpect(jsonPath("$.content[0].id").value(userDtoList.get(0).getId()))
                .andExpect(jsonPath("$.content[1].id").value(userDtoList.get(1).getId()));
        verify(userService).getSortedPage(PAGE, SIZE, SORT_BY, ORDER);
    }

    @Test
    void givenInvalidPageableData_whenGetSortedPage_thenReturnValidationExceptionListJson() throws Exception {
        mockMvc.perform(get("/user")
                        .queryParam("page", String.valueOf(INVALID_PAGE))
                        .queryParam("size", String.valueOf(INVALID_SIZE))
                        .queryParam("sortBy", INVALID_SORT_BY)
                        .queryParam("order", INVALID_ORDER))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].exceptionType").value(ExceptionType.VALIDATION_EXCEPTION.name()))
                .andExpect(jsonPath("$[1].exceptionType").value(ExceptionType.VALIDATION_EXCEPTION.name()))
                .andExpect(jsonPath("$[2].exceptionType").value(ExceptionType.VALIDATION_EXCEPTION.name()))
                .andExpect(jsonPath("$[3].exceptionType").value(ExceptionType.VALIDATION_EXCEPTION.name()));
        verify(userService, never()).getSortedPage(PAGE, SIZE, SORT_BY, ORDER);
    }

    @Test
    void givenValidUserDto_whenCreate_thenReturnCreatedUserDto() throws Exception {
        UserDto beforeCreate = createUserDto();
        beforeCreate.setId(null);
        beforeCreate.setPassword(PASSWORD);
        UserDto afterCreate = createUserDto();
        afterCreate.setId(ID);
        when(userService.create(beforeCreate)).thenReturn(afterCreate);

        mockMvc.perform(post("/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(beforeCreate)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(afterCreate.getId()))
                .andExpect(jsonPath("$.username").value(afterCreate.getUsername()))
                .andExpect(jsonPath("$.email").value(afterCreate.getEmail()))
                .andExpect(jsonPath("$.books").doesNotExist());
        verify(userService).create(beforeCreate);
    }

    @Test
    void givenInvalidUserDto_whenCreate_thenReturnValidationExceptionListJson() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(ID);
        userDto.setBooks(new HashSet<>());
        mockMvc.perform(post("/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].exceptionType").value(ExceptionType.VALIDATION_EXCEPTION.name()))
                .andExpect(jsonPath("$[1].exceptionType").value(ExceptionType.VALIDATION_EXCEPTION.name()))
                .andExpect(jsonPath("$[2].exceptionType").value(ExceptionType.VALIDATION_EXCEPTION.name()))
                .andExpect(jsonPath("$[3].exceptionType").value(ExceptionType.VALIDATION_EXCEPTION.name()))
                .andExpect(jsonPath("$[4].exceptionType").value(ExceptionType.VALIDATION_EXCEPTION.name()));
        verify(userService, never()).create(userDto);
    }

    @Test
    void givenValidUserDto_whenUpdate_thenReturnUpdatedUserDto() throws Exception {
        UserDto beforeUpdate = createUserDto();
        beforeUpdate.setUsername(null);
        UserDto afterUpdate = createUserDto();
        when(userService.update(beforeUpdate)).thenReturn(afterUpdate);

        mockMvc.perform(patch("/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(beforeUpdate)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(afterUpdate.getId()))
                .andExpect(jsonPath("$.username").value(afterUpdate.getUsername()))
                .andExpect(jsonPath("$.email").value(afterUpdate.getEmail()))
                .andExpect(jsonPath("$.books").doesNotExist());
        verify(userService).update(beforeUpdate);
    }

    @Test
    void givenInvalidUserDto_whenUpdate_thenReturnValidationExceptionListJson() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(null);
        userDto.setUsername(USERNAME);
        userDto.setBooks(new HashSet<>());
        mockMvc.perform(patch("/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].exceptionType").value(ExceptionType.VALIDATION_EXCEPTION.name()))
                .andExpect(jsonPath("$[1].exceptionType").value(ExceptionType.VALIDATION_EXCEPTION.name()))
                .andExpect(jsonPath("$[2].exceptionType").value(ExceptionType.VALIDATION_EXCEPTION.name()));
        verify(userService, never()).create(userDto);
    }

    @Test
    void givenValidUserId_whenDelete_thenDeleteAndReturnNothing() throws Exception {
        doNothing().when(userService).deleteById(ID);

        mockMvc.perform(delete("/user/{id}", ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(userService).deleteById(ID);
    }

    @Test
    void givenInvalidUserId_whenDelete_thenReturnValidationExceptionListJson() throws Exception {
        mockMvc.perform(delete("/user/{id}", INVALID_ID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].exceptionType").value(ExceptionType.VALIDATION_EXCEPTION.name()));
        verify(userService, never()).deleteById(INVALID_ID);
    }
}
