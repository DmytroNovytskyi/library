package com.my.library.controller.api;

import com.my.library.dto.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/book")
public interface BookApi {
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    BookDto getById(@PathVariable("id") long id);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<BookDto> getSortedPage(@RequestParam("page") int page,
                                @RequestParam("size") int size,
                                @RequestParam("sortBy") String sortBy,
                                @RequestParam("order") String order);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    BookDto create(@RequestBody BookDto book);

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    BookDto update(@RequestBody BookDto book);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable("id") long id);
}
