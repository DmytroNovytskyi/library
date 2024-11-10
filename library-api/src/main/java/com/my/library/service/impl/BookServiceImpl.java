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

    /**
     * The BookRepository object that will be used to interact with books in database.
     */
    private final BookRepository bookRepository;

    /**
     * Returns a book with the given ID.
     *
     * @param id the ID of the book to retrieve
     * @return the book with the given ID
     * @throws BookNotFoundException if the book with the specified ID cannot be found
     */
    @Transactional
    @Override
    public BookDto getById(long id) {
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        return BookMapper.INSTANCE.mapBookDto(book);
    }

    /**
     * Retrieves a sorted and paginated list of books.
     *
     * @param page   the page number of the results to retrieve
     * @param size   the maximum number of results per page
     * @param sortBy the field to sort the results by
     * @param order  the sort order
     * @return a page of books sorted and filtered according to the specified parameters
     */
    @Transactional
    @Override
    public Page<BookDto> getSortedPage(int page, int size, String sortBy, String order) {
        Pageable pageable = PageRequest.of(page, size, order.equals("desc")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
        return bookRepository.findAll(pageable).map(BookMapper.INSTANCE::mapBookDto);
    }

    /**
     * Creates a new book.
     *
     * @param bookDto the book to create
     * @return the newly created book
     * @throws BookAlreadyExistsException if a book with given author and name already exists
     */
    @Transactional
    @Override
    public BookDto create(BookDto bookDto) {
        Book book = BookMapper.INSTANCE.mapBook(bookDto);
        if (bookRepository.existsByAuthorAndName(book.getAuthor(), book.getName())) {
            throw new BookAlreadyExistsException();
        }
        return BookMapper.INSTANCE.mapBookDto(bookRepository.save(book));
    }

    /**
     * Updates an existing book.
     *
     * @param bookDto the book to update
     * @return the updated book
     * @throws BookNotFoundException      if the book with the specified ID cannot be found
     * @throws BookAlreadyExistsException if a book with given author and name already exists
     */
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

    /**
     * Deletes the Book entity with the specified ID.
     *
     * @param id the ID of the book to delete
     * @throws BookNotFoundException if the book with the specified ID cannot be found
     * @throws IssuedBookException   if the book with the specified ID is issued to a user
     */
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
