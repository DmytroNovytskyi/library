package com.my.library.service.impl;

import com.my.library.dto.BookDto;
import com.my.library.mapper.BookMapper;
import com.my.library.model.Book;
import com.my.library.repository.BookRepository;
import com.my.library.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookDto getById(long id) {
        Book book = bookRepository.findById(id).orElse(null);
        return BookMapper.INSTANCE.mapBookDto(book);
    }

    @Override
    public Page<BookDto> getSortedPage(int page, int size, String sortBy, String order) {
        Pageable pageable = PageRequest.of(page, size, order.equals("desc")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
        return bookRepository.findAll(pageable).map(BookMapper.INSTANCE::mapBookDto);
    }

    @Override
    public BookDto create(BookDto bookDto) {
        Book created = bookRepository.save(BookMapper.INSTANCE.mapBook(bookDto));
        return BookMapper.INSTANCE.mapBookDto(created);
    }

    @Override
    public BookDto update(BookDto book) {
        Book persisted = bookRepository.findById(book.getId()).orElse(null);
        if (persisted == null) {
            throw new RuntimeException("Book was not found!");
        }
        Book updating = BookMapper.INSTANCE.mapBook(book);
        BookMapper.INSTANCE.mapPresentFields(persisted, updating);
        return BookMapper.INSTANCE.mapBookDto(bookRepository.save(persisted));
    }

    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }
}
