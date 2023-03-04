package com.my.library.controller;

import com.my.library.controller.api.BookApi;
import com.my.library.dto.BookDto;
import com.my.library.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController implements BookApi {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public BookDto getById(long id) {
        return bookService.getById(id);
    }

    @Override
    public Page<BookDto> getSortedPage(int page, int size, String sortBy, String order) {
        return bookService.getSortedPage(page, size, sortBy, order);
    }

    @Override
    public BookDto create(BookDto book) {
        return bookService.create(book);
    }

    @Override
    public BookDto update(BookDto book) {
        return bookService.update(book);
    }

    @Override
    public ResponseEntity<Void> delete(long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
