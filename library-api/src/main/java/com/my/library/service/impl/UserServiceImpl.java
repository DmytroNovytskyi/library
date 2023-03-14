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
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional
    @Override
    public UserDto getById(long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return UserMapper.INSTANCE.mapUserDto(user);
    }

    @Transactional
    @Override
    public Page<UserDto> getSortedPage(int page, int size, String sortBy, String order) {
        Pageable pageable = PageRequest.of(page, size, order.equals("desc")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
        return userRepository.findAll(pageable).map(UserMapper.INSTANCE::mapUserDto);
    }

    @Transactional
    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.INSTANCE.mapUser(userDto);
        if (userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail())) {
            throw new UserAlreadyExistsException();
        }
        return UserMapper.INSTANCE.mapUserDto(userRepository.save(user));
    }

    @Transactional
    @Override
    public UserDto update(UserDto userDto) {
        User updating = UserMapper.INSTANCE.mapUser(userDto);
        User persisted = userRepository.findById(updating.getId()).orElseThrow(UserNotFoundException::new);
        String updatingUsername = updating.getUsername() == null ? persisted.getUsername() : updating.getUsername();
        String updatingEmail = updating.getEmail() == null ? persisted.getEmail() : updating.getEmail();
        if (userRepository.existsByUsernameOrEmailAndIdIsNot(updatingUsername, updatingEmail, persisted.getId())) {
            throw new UserAlreadyExistsException();
        }
        UserMapper.INSTANCE.mapPresentFields(persisted, updating);
        return UserMapper.INSTANCE.mapUserDto(userRepository.save(persisted));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        if (!user.getBooks().isEmpty()) {
            throw new UserIssuedBookException();
        }
        userRepository.deleteById(id);
    }

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
