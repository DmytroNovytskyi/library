package com.my.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.library.dto.BookDto;
import com.my.library.exception.wrapper.ExceptionType;
import com.my.library.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.my.library.util.BookTestData.*;
import static com.my.library.util.CommonTestData.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void givenValidBookId_whenGetById_thenReturnBookDto() throws Exception {
        when(bookService.getById(ID)).thenReturn(createBookDto());

        mockMvc.perform(get("/book/{id}", ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.author").value(AUTHOR))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.available").value(AVAILABLE));
        verify(bookService).getById(ID);
    }

    @Test
    void givenInvalidBookId_whenGetById_thenReturnValidationExceptionListJson() throws Exception {
        mockMvc.perform(get("/book/{id}", INVALID_ID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].exceptionType").value(ExceptionType.VALIDATION_EXCEPTION.name()));
        verify(bookService, never()).getById(INVALID_ID);
    }

    @Test
    void givenValidPageableData_whenGetSortedPage_thenReturnPageOfBooks() throws Exception {
        List<BookDto> bookDtoList = createBookDtoList();
        Page<BookDto> bookDtoPage = createPage(bookDtoList);
        when(bookService.getSortedPage(PAGE, SIZE, SORT_BY, ORDER)).thenReturn(bookDtoPage);

        mockMvc.perform(get("/book")
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
                .andExpect(jsonPath("$.content[0].id").value(bookDtoList.get(0).getId()))
                .andExpect(jsonPath("$.content[1].id").value(bookDtoList.get(1).getId()));
        verify(bookService).getSortedPage(PAGE, SIZE, SORT_BY, ORDER);
    }

    @Test
    void givenInvalidPageableData_whenGetSortedPage_thenReturnValidationExceptionListJson() throws Exception {
        mockMvc.perform(get("/book")
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
        verify(bookService, never()).getSortedPage(PAGE, SIZE, SORT_BY, ORDER);
    }

    @Test
    void givenValidBookDto_whenCreate_thenReturnCreatedBookDto() throws Exception {
        BookDto beforeCreate = createBookDto();
        beforeCreate.setId(null);
        BookDto afterCreate = createBookDto();
        afterCreate.setId(ID);
        when(bookService.create(beforeCreate)).thenReturn(afterCreate);

        mockMvc.perform(post("/book")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(beforeCreate)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(afterCreate.getId()))
                .andExpect(jsonPath("$.author").value(afterCreate.getAuthor()))
                .andExpect(jsonPath("$.name").value(afterCreate.getName()))
                .andExpect(jsonPath("$.available").value(afterCreate.getAvailable()));
        verify(bookService).create(beforeCreate);
    }

    @Test
    void givenInvalidBookDto_whenCreate_thenReturnValidationExceptionListJson() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setId(ID);
        mockMvc.perform(post("/book")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].exceptionType").value(ExceptionType.VALIDATION_EXCEPTION.name()))
                .andExpect(jsonPath("$[1].exceptionType").value(ExceptionType.VALIDATION_EXCEPTION.name()))
                .andExpect(jsonPath("$[2].exceptionType").value(ExceptionType.VALIDATION_EXCEPTION.name()))
                .andExpect(jsonPath("$[3].exceptionType").value(ExceptionType.VALIDATION_EXCEPTION.name()));
        verify(bookService, never()).create(bookDto);
    }

    @Test
    void givenValidBookDto_whenUpdate_thenReturnUpdatedBookDto() throws Exception {
        BookDto beforeUpdate = createBookDto();
        beforeUpdate.setAvailable(null);
        BookDto afterUpdate = createBookDto();
        when(bookService.update(beforeUpdate)).thenReturn(afterUpdate);

        mockMvc.perform(patch("/book")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(beforeUpdate)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(afterUpdate.getId()))
                .andExpect(jsonPath("$.author").value(afterUpdate.getAuthor()))
                .andExpect(jsonPath("$.name").value(afterUpdate.getName()))
                .andExpect(jsonPath("$.available").value(afterUpdate.getAvailable()));
        verify(bookService).update(beforeUpdate);
    }

    @Test
    void givenInvalidBookDto_whenUpdate_thenReturnValidationExceptionListJson() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setAvailable(AVAILABLE);
        mockMvc.perform(patch("/book")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].exceptionType").value(ExceptionType.VALIDATION_EXCEPTION.name()))
                .andExpect(jsonPath("$[1].exceptionType").value(ExceptionType.VALIDATION_EXCEPTION.name()));
        verify(bookService, never()).create(bookDto);
    }

    @Test
    void givenValidBookId_whenDelete_thenDeleteAndReturnNothing() throws Exception {
        doNothing().when(bookService).deleteById(ID);

        mockMvc.perform(delete("/book/{id}", ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(bookService).deleteById(ID);
    }

    @Test
    void givenInvalidBookId_whenDelete_thenReturnValidationExceptionListJson() throws Exception {
        mockMvc.perform(delete("/book/{id}", INVALID_ID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].exceptionType").value(ExceptionType.VALIDATION_EXCEPTION.name()));
        verify(bookService, never()).deleteById(INVALID_ID);
    }
}
