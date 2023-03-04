package com.my.library.service;

import com.my.library.dto.BookDto;
import org.springframework.data.domain.Page;

public interface BookService {
    BookDto getById(long id);

    Page<BookDto> getSortedPage(int page, int size, String sortBy, String order);

    BookDto create(BookDto bookDto);

    BookDto update(BookDto bookDto);

    void deleteById(long id);
}
