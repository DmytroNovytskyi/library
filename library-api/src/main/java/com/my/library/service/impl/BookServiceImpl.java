package com.my.library.service.impl;

import com.my.library.dto.BookDto;
import com.my.library.exception.BookAlreadyExistsException;
import com.my.library.exception.BookNotFoundException;
import com.my.library.exception.IssuedBookException;
import com.my.library.mapper.BookMapper;
import com.my.library.model.Book;
import com.my.library.repository.BookRepository;
import com.my.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Transactional
    @Override
    public BookDto getById(long id) {
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        return BookMapper.INSTANCE.mapBookDto(book);
    }

    @Transactional
    @Override
    public Page<BookDto> getSortedPage(int page, int size, String sortBy, String order) {
        Pageable pageable = PageRequest.of(page, size, order.equals("desc")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
        return bookRepository.findAll(pageable).map(BookMapper.INSTANCE::mapBookDto);
    }

    @Transactional
    @Override
    public BookDto create(BookDto bookDto) {
        Book book = BookMapper.INSTANCE.mapBook(bookDto);
        if (bookRepository.existsByAuthorAndName(book.getAuthor(), book.getName())) {
            throw new BookAlreadyExistsException();
        }
        return BookMapper.INSTANCE.mapBookDto(bookRepository.save(book));
    }

    @Transactional
    @Override
    public BookDto update(BookDto bookDto) {
        Book updating = BookMapper.INSTANCE.mapBook(bookDto);
        Book persisted = bookRepository.findById(updating.getId()).orElseThrow(BookNotFoundException::new);
        String updatingAuthor = updating.getAuthor() == null ? persisted.getAuthor() : updating.getAuthor();
        String updatingName = updating.getName() == null ? persisted.getName() : updating.getName();
        if (bookRepository.existsByAuthorAndNameAndIdIsNot(updatingAuthor, updatingName, updating.getId())) {
            throw new BookAlreadyExistsException();
        }
        BookMapper.INSTANCE.mapPresentFields(persisted, updating);
        return BookMapper.INSTANCE.mapBookDto(bookRepository.save(persisted));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        if (!book.getUsers().isEmpty()) {
            throw new IssuedBookException();
        }
        bookRepository.deleteById(id);
    }
}
