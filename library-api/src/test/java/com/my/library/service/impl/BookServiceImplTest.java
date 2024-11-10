package com.my.library.service.impl;

import com.my.library.dto.BookDto;
import com.my.library.exception.BookAlreadyExistsException;
import com.my.library.exception.BookNotFoundException;
import com.my.library.exception.IssuedBookException;
import com.my.library.model.Book;
import com.my.library.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.Optional;

import static com.my.library.util.BookTestData.*;
import static com.my.library.util.CommonTestData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {
    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Test
    void givenValidBookId_whenGetById_thenReturnBookDto() {
        BookDto expected = createBookDto();

        when(bookRepository.findById(ID)).thenReturn(Optional.of(createBook()));

        BookDto actual = bookService.getById(ID);

        assertThat(actual, is(expected));
        verify(bookRepository).findById(ID);
    }

    @Test
    void givenInvalidBookId_whenGetById_thenThrowBookNotFoundException() {
        when(bookRepository.findById(ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(BookNotFoundException.class)
                .isThrownBy(() -> bookService.getById(ID));
        verify(bookRepository).findById(ID);
    }

    @Test
    void givenPageableData_whenGetSortedPage_thenReturnPageOfBooks() {
        Pageable pageable = createPageable();
        Page<Book> bookPage = createPage(createBookList());
        Page<BookDto> expected = createPage(createBookDtoList());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        Page<BookDto> actual = bookService.getSortedPage(PAGE, SIZE, SORT_BY, ORDER);

        assertThat(actual.getSize(), is(SIZE));
        assertThat(actual.getPageable(), is(pageable));
        assertThat(actual, is(expected));
        verify(bookRepository).findAll(pageable);
    }

    @Test
    void givenBookDtoWithExistingNameForAuthor_whenCreate_thenThrowBookAlreadyExistsException() {
        when(bookRepository.existsByAuthorAndName(AUTHOR, NAME)).thenReturn(true);

        assertThatExceptionOfType(BookAlreadyExistsException.class)
                .isThrownBy(() -> bookService.create(createBookDto()));
        verify(bookRepository).existsByAuthorAndName(AUTHOR, NAME);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void givenValidBookDto_whenCreate_thenReturnCreatedBookDto() {
        BookDto bookDto = createBookDto();
        Book book = createBook();
        BookDto expected = createBookDto();

        when(bookRepository.save(book)).thenReturn(createBook());

        BookDto actual = bookService.create(bookDto);

        assertThat(actual, is(expected));
        verify(bookRepository).save(book);
    }

    @Test
    void givenBookDtoWithInvalidId_whenUpdate_thenThrowBookNotFoundException() {
        BookDto bookDto = createBookDto();

        when(bookRepository.findById(ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(BookNotFoundException.class)
                .isThrownBy(() -> bookService.update(bookDto));
        verify(bookRepository).findById(ID);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void givenBookDtoWithDuplicateAuthorNamePair_whenUpdate_thenThrowBookAlreadyExistsException() {
        BookDto bookDto = createBookDto();
        Book book = createBook();

        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        when(bookRepository.existsByAuthorAndNameAndIdIsNot(AUTHOR, NAME, ID)).thenReturn(true);

        assertThatExceptionOfType(BookAlreadyExistsException.class)
                .isThrownBy(() -> bookService.update(bookDto));
        verify(bookRepository).findById(ID);
        verify(bookRepository).existsByAuthorAndNameAndIdIsNot(AUTHOR, NAME, ID);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void givenValidBookDto_whenUpdate_thenReturnUpdatedBookDto() {
        BookDto expected = createBookDto();
        Book book = createBook();

        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        when(bookRepository.existsByAuthorAndNameAndIdIsNot(AUTHOR, NAME, ID)).thenReturn(false);
        when(bookRepository.save(book)).thenReturn(book);

        BookDto actual = bookService.update(expected);
        assertThat(actual, is(expected));
        verify(bookRepository).findById(ID);
        verify(bookRepository).existsByAuthorAndNameAndIdIsNot(AUTHOR, NAME, ID);
        verify(bookRepository).save(book);
    }

    @Test
    void givenInvalidBookId_whenDelete_thenThrowBookNotFoundException() {
        when(bookRepository.findById(ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(BookNotFoundException.class)
                .isThrownBy(() -> bookService.deleteById(ID));
        verify(bookRepository).findById(ID);
        verify(bookRepository, never()).deleteById(ID);
    }

    @Test
    void givenValidBookIdForIssuedBook_whenDelete_thenThrowIssuedBookException() {
        Book book = createBook();
        book.setUsers(USERS);
        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));

        assertThatExceptionOfType(IssuedBookException.class)
                .isThrownBy(() -> bookService.deleteById(ID));
        verify(bookRepository).findById(ID);
        verify(bookRepository, never()).deleteById(ID);
    }

    @Test
    void givenValidBookId_whenDelete_thenRepositoryMethodCall() {
        Book book = createBook();
        book.setUsers(new HashSet<>());
        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).deleteById(ID);

        bookService.deleteById(ID);

        verify(bookRepository).findById(ID);
        verify(bookRepository).deleteById(ID);
    }
}
