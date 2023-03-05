package com.my.library.service.impl;

import com.my.library.dto.UserDto;
import com.my.library.mapper.UserMapper;
import com.my.library.model.Book;
import com.my.library.model.User;
import com.my.library.repository.BookRepository;
import com.my.library.repository.UserRepository;
import com.my.library.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public UserServiceImpl(UserRepository userRepository,
                           BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    @Override
    public UserDto getById(long id) {
        User user = userRepository.findById(id).orElse(null);
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
        User created = userRepository.save(UserMapper.INSTANCE.mapUser(userDto));
        return UserMapper.INSTANCE.mapUserDto(created);
    }

    @Transactional
    @Override
    public UserDto update(UserDto userDto) {
        User persisted = userRepository.findById(userDto.getId()).orElse(null);
        if (persisted == null) {
            throw new RuntimeException("User was not found!");
        }
        User updating = UserMapper.INSTANCE.mapUser(userDto);
        UserMapper.INSTANCE.mapPresentFields(persisted, updating);
        return UserMapper.INSTANCE.mapUserDto(userRepository.save(persisted));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User was not found!"));
        if (user.getBooks().isEmpty()) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User has issued books!");
        }
    }

    @Transactional
    @Override
    public UserDto issueBook(long userId, long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User was not found!"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book was not found!"));
        int available = book.getAvailable();
        if (available == 0) {
            throw new RuntimeException("No available books!");
        }
        if (user.getBooks().add(book)) {
            book.setAvailable(available - 1);
            bookRepository.save(book);
        } else {
            throw new RuntimeException("Book had been already issued to user!");
        }
        return UserMapper.INSTANCE.mapUserDto(userRepository.save(user));
    }

    @Transactional
    @Override
    public UserDto returnBook(long userId, long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User was not found!"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book was not found!"));
        if (user.getBooks().remove(book)) {
            book.setAvailable(book.getAvailable() + 1);
            bookRepository.save(book);
        } else {
            throw new RuntimeException("Book was not issued to user!");
        }
        return UserMapper.INSTANCE.mapUserDto(userRepository.save(user));
    }
}
