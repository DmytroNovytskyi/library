package com.my.library.util;

import com.my.library.dto.BookDto;
import com.my.library.model.Book;
import com.my.library.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookTestData {
    public static final Long ID = 1L;
    public static final Long INVALID_ID = 0L;
    public static final String AUTHOR = "test author";
    public static final String NAME = "test name";
    public static final Integer AVAILABLE = 100;
    public static final Set<User> USERS = new HashSet<>(List.of(new User()));

    public static Book createBook() {
        Book book = new Book();
        book.setId(ID);
        book.setAuthor(AUTHOR);
        book.setName(NAME);
        book.setAvailable(AVAILABLE);
        return book;
    }

    public static BookDto createBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(ID);
        bookDto.setAuthor(AUTHOR);
        bookDto.setName(NAME);
        bookDto.setAvailable(AVAILABLE);
        return bookDto;
    }

    public static List<Book> createBookList() {
        List<Book> bookList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Book book = new Book();
            book.setId(ID + i);
            book.setAuthor(AUTHOR + "a".repeat(i));
            book.setName(NAME + "a".repeat(i));
            book.setAvailable(AVAILABLE + i);
            bookList.add(book);
        }
        return bookList;
    }

    public static List<BookDto> createBookDtoList() {
        List<BookDto> bookDtoList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            BookDto bookDto = new BookDto();
            bookDto.setId(ID + i);
            bookDto.setAuthor(AUTHOR + "a".repeat(i));
            bookDto.setName(NAME + "a".repeat(i));
            bookDto.setAvailable(AVAILABLE + i);
            bookDtoList.add(bookDto);
        }
        return bookDtoList;
    }
}
