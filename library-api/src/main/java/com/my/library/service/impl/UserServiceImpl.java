package com.my.library.service.impl;

import com.my.library.dto.UserDto;
import com.my.library.exception.*;
import com.my.library.mapper.UserMapper;
import com.my.library.model.Book;
import com.my.library.model.User;
import com.my.library.repository.BookRepository;
import com.my.library.repository.UserRepository;
import com.my.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    /**
     * The UserRepository object that will be used to interact with users in database.
     */
    private final UserRepository userRepository;

    /**
     * The BookRepository object that will be used to interact with books in database.
     */
    private final BookRepository bookRepository;

    /**
     * Retrieves a user with the specified ID.
     *
     * @param id the ID of the user to retrieve
     * @return the user with the specified ID
     * @throws UserNotFoundException if the user with the specified ID is not found
     */
    @Transactional
    @Override
    public UserDto getById(long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return UserMapper.INSTANCE.mapUserDto(user);
    }

    /**
     * Retrieves a sorted and paginated list of users
     *
     * @param page   the page number of the results to retrieve
     * @param size   the maximum number of results per page
     * @param sortBy the field to sort the results by
     * @param order  the sort order
     * @return a page of users sorted and filtered according to the specified parameters
     */
    @Transactional
    @Override
    public Page<UserDto> getSortedPage(int page, int size, String sortBy, String order) {
        Pageable pageable = PageRequest.of(page, size, order.equals("desc")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
        return userRepository.findAll(pageable).map(UserMapper.INSTANCE::mapUserDto);
    }

    /**
     * Creates a new user
     *
     * @param userDto the user to create
     * @return the newly created user
     * @throws UserAlreadyExistsException if a user with the same username or email already exists
     */
    @Transactional
    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.INSTANCE.mapUser(userDto);
        if (userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail())) {
            throw new UserAlreadyExistsException();
        }
        return UserMapper.INSTANCE.mapUserDto(userRepository.save(user));
    }

    /**
     * Updates an existing user.
     *
     * @param userDto the user to update
     * @return the updated user.
     * @throws UserNotFoundException      if the user with the specified ID is not found.
     * @throws UserAlreadyExistsException if a user with the same email already exists.
     */
    @Transactional
    @Override
    public UserDto update(UserDto userDto) {
        User updating = UserMapper.INSTANCE.mapUser(userDto);
        User persisted = userRepository.findById(updating.getId()).orElseThrow(UserNotFoundException::new);
        String updatingEmail = updating.getEmail() == null ? persisted.getEmail() : updating.getEmail();
        if (userRepository.existsByEmailAndIdIsNot(updatingEmail, persisted.getId())) {
            throw new UserAlreadyExistsException();
        }
        UserMapper.INSTANCE.mapPresentFields(persisted, updating);
        return UserMapper.INSTANCE.mapUserDto(userRepository.save(persisted));
    }

    /**
     * Deletes a user with the specified ID.
     *
     * @param id the ID of the user to delete
     * @throws UserNotFoundException   if the user with the specified ID is not found
     * @throws UserIssuedBookException if the user with the specified ID has issued books
     */
    @Transactional
    @Override
    public void deleteById(long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        if (!user.getBooks().isEmpty()) {
            throw new UserIssuedBookException();
        }
        userRepository.deleteById(id);
    }

    /**
     * Issues a book to a user.
     *
     * @param userId the ID of the user to issue the book to
     * @param bookId the ID of the book to issue
     * @return the updated user
     * @throws UserNotFoundException      if the user with the specified ID is not found
     * @throws BookNotFoundException      if the book with the specified ID cannot be found
     * @throws NoAvailableBooksException  if the book with the specified ID has no available copies left
     * @throws BookAlreadyIssuedException if the book with the specified ID is already issued to the user
     */
    @Transactional
    @Override
    public UserDto issueBook(long userId, long bookId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        int available = book.getAvailable();
        if (available == 0) {
            throw new NoAvailableBooksException();
        }
        if (!user.getBooks().add(book)) {
            throw new BookAlreadyIssuedException();
        }
        book.setAvailable(available - 1);
        bookRepository.save(book);
        return UserMapper.INSTANCE.mapUserDto(userRepository.save(user));
    }

    /**
     * Returns a book from a user.
     *
     * @param userId the ID of the user to return the book from
     * @param bookId the ID of the book to return
     * @return the updated user
     * @throws UserNotFoundException  if the user with the specified ID is not found
     * @throws BookNotFoundException  if the book with the specified ID cannot be found
     * @throws BookNotIssuedException if the book with the specified ID is not issued to the user
     */
    @Transactional
    @Override
    public UserDto returnBook(long userId, long bookId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        if (!user.getBooks().remove(book)) {
            throw new BookNotIssuedException();
        }
        book.setAvailable(book.getAvailable() + 1);
        bookRepository.save(book);
        return UserMapper.INSTANCE.mapUserDto(userRepository.save(user));
    }
}
