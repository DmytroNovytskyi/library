package com.my.library.service.impl;

import com.my.library.dto.BookDto;
import com.my.library.dto.UserDto;
import com.my.library.exception.*;
import com.my.library.model.Book;
import com.my.library.model.User;
import com.my.library.repository.BookRepository;
import com.my.library.repository.UserRepository;
import com.my.library.util.BookTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.my.library.util.BookTestData.createBook;
import static com.my.library.util.BookTestData.createBookDto;
import static com.my.library.util.CommonTestData.*;
import static com.my.library.util.UserTestData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    void givenValidUserId_whenGetById_thenReturnUserDto() {
        UserDto expected = createUserDto();

        when(userRepository.findById(ID)).thenReturn(Optional.of(createUser()));

        UserDto actual = userService.getById(ID);

        assertThat(actual, is(expected));
        verify(userRepository).findById(ID);
    }

    @Test
    void givenInvalidUserId_whenGetById_thenThrowUserNotFoundException() {
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.getById(ID));
        verify(userRepository).findById(ID);
    }

    @Test
    void givenPageableData_whenGetSortedPage_thenReturnPageOfUsers() {
        Pageable pageable = createPageable();
        Page<User> userPage = createPage(createUserList());
        Page<UserDto> expected = createPage(createUserDtoList());

        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<UserDto> actual = userService.getSortedPage(PAGE, SIZE, SORT_BY, ORDER);

        assertThat(actual.getSize(), is(SIZE));
        assertThat(actual.getPageable(), is(pageable));
        assertThat(actual, is(expected));
        verify(userRepository).findAll(pageable);
    }

    @Test
    void givenUserDtoWithExistingUsernameOrEmail_whenCreate_thenThrowUserAlreadyExistsException() {
        when(userRepository.existsByUsernameOrEmail(USERNAME, EMAIL)).thenReturn(true);

        assertThatExceptionOfType(UserAlreadyExistsException.class)
                .isThrownBy(() -> userService.create(createUserDto()));
        verify(userRepository).existsByUsernameOrEmail(USERNAME, EMAIL);
        verify(userRepository, never()).save(any());
    }

    @Test
    void givenValidUserDto_whenCreate_thenReturnCreatedUserDto() {
        UserDto userDto = createUserDto();
        User user = createUser();
        UserDto expected = createUserDto();

        when(userRepository.save(user)).thenReturn(createUser());

        UserDto actual = userService.create(userDto);

        assertThat(actual, is(expected));
        verify(userRepository).save(user);
    }

    @Test
    void givenUserDtoWithInvalidId_whenUpdate_thenThrowUserNotFoundException() {
        UserDto userDto = createUserDto();

        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.update(userDto));
        verify(userRepository).findById(ID);
        verify(userRepository, never()).save(any());
    }

    @Test
    void givenUserDtoWithExistingUsernameOrEmail_whenUpdate_thenThrowUserAlreadyExistsException() {
        UserDto userDto = createUserDto();
        User user = createUser();

        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsernameOrEmailAndIdIsNot(USERNAME, EMAIL, ID)).thenReturn(true);

        assertThatExceptionOfType(UserAlreadyExistsException.class)
                .isThrownBy(() -> userService.update(userDto));
        verify(userRepository).findById(ID);
        verify(userRepository).existsByUsernameOrEmailAndIdIsNot(USERNAME, EMAIL, ID);
        verify(userRepository, never()).save(any());
    }

    @Test
    void givenValidUserDto_whenUpdate_thenReturnUpdatedUserDto() {
        UserDto expected = createUserDto();
        User user = createUser();

        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsernameOrEmailAndIdIsNot(USERNAME, EMAIL, ID)).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        UserDto actual = userService.update(expected);
        assertThat(actual, is(expected));
        verify(userRepository).findById(ID);
        verify(userRepository).existsByUsernameOrEmailAndIdIsNot(USERNAME, EMAIL, ID);
        verify(userRepository).save(user);
    }

    @Test
    void givenInvalidUserId_whenDelete_thenThrowUserNotFoundException() {
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.deleteById(ID));
        verify(userRepository).findById(ID);
        verify(userRepository, never()).deleteById(ID);
    }

    @Test
    void givenValidUserIdForUserWithIssuedBook_whenDelete_thenThrowUserIssuedBookException() {
        User user = createUser();
        user.setBooks(BOOKS);
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        assertThatExceptionOfType(UserIssuedBookException.class)
                .isThrownBy(() -> userService.deleteById(ID));
        verify(userRepository).findById(ID);
        verify(userRepository, never()).deleteById(ID);
    }

    @Test
    void givenValidUserId_whenDelete_thenRepositoryMethodCall() {
        User user = createUser();
        user.setBooks(new HashSet<>());
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(ID);

        userService.deleteById(ID);

        verify(userRepository).findById(ID);
        verify(userRepository).deleteById(ID);
    }

    @Test
    void givenInvalidUserId_whenIssueBook_thenThrowUserNotFoundException() {
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.issueBook(ID, BookTestData.ID));
        verify(userRepository).findById(ID);
        verify(bookRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void givenInvalidBookId_whenIssueBook_thenThrowBookNotFoundException() {
        when(bookRepository.findById(BookTestData.INVALID_ID)).thenReturn(Optional.empty());
        when(userRepository.findById(ID)).thenReturn(Optional.of(createUser()));

        assertThatExceptionOfType(BookNotFoundException.class)
                .isThrownBy(() -> userService.issueBook(ID, BookTestData.INVALID_ID));
        verify(bookRepository).findById(BookTestData.INVALID_ID);
        verify(userRepository).findById(ID);
        verify(bookRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void givenBookWithZeroAvailable_whenIssueBook_thenThrowNoAvailableBooksException() {
        Book book = createBook();
        book.setAvailable(0);
        when(bookRepository.findById(BookTestData.ID)).thenReturn(Optional.of(book));
        when(userRepository.findById(ID)).thenReturn(Optional.of(createUser()));

        assertThatExceptionOfType(NoAvailableBooksException.class)
                .isThrownBy(() -> userService.issueBook(ID, BookTestData.ID));
        verify(bookRepository).findById(BookTestData.ID);
        verify(userRepository).findById(ID);
        verify(bookRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void givenAlreadyIssuedBook_whenIssueBook_thenThrowBookAlreadyIssuedException() {
        Book book = createBook();
        User user = createUser();
        user.setBooks(new HashSet<>(List.of(book)));
        when(bookRepository.findById(BookTestData.ID)).thenReturn(Optional.of(book));
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        assertThatExceptionOfType(BookAlreadyIssuedException.class)
                .isThrownBy(() -> userService.issueBook(ID, BookTestData.ID));
        verify(bookRepository).findById(BookTestData.ID);
        verify(userRepository).findById(ID);
        verify(bookRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void givenValidUserAndBook_whenIssueBook_thenReturnUpdatedUserDto() {
        Book book = createBook();
        User user = createUser();
        user.setBooks(new HashSet<>());
        Book updatedBook = createBook();
        updatedBook.setAvailable(updatedBook.getAvailable() - 1);
        User updatedUser = createUser();
        updatedUser.setBooks(new HashSet<>(List.of(updatedBook)));

        BookDto expectedBook = createBookDto();
        expectedBook.setAvailable(expectedBook.getAvailable() - 1);
        UserDto expectedUser = createUserDto();
        expectedUser.setBooks(new HashSet<>(List.of(expectedBook)));

        when(bookRepository.findById(BookTestData.ID)).thenReturn(Optional.of(book));
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        UserDto actual = userService.issueBook(ID, BookTestData.ID);

        assertThat(actual, is(expectedUser));
        verify(bookRepository).findById(BookTestData.ID);
        verify(userRepository).findById(ID);
        verify(bookRepository).save(updatedBook);
        verify(userRepository).save(updatedUser);
    }

    @Test
    void givenInvalidUserId_whenReturnBook_thenThrowUserNotFoundException() {
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.returnBook(ID, BookTestData.ID));
        verify(userRepository).findById(ID);
        verify(bookRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void givenInvalidBookId_whenReturnBook_thenThrowBookNotFoundException() {
        when(bookRepository.findById(BookTestData.INVALID_ID)).thenReturn(Optional.empty());
        when(userRepository.findById(ID)).thenReturn(Optional.of(createUser()));

        assertThatExceptionOfType(BookNotFoundException.class)
                .isThrownBy(() -> userService.returnBook(ID, BookTestData.INVALID_ID));
        verify(bookRepository).findById(BookTestData.INVALID_ID);
        verify(userRepository).findById(ID);
        verify(bookRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void givenNotIssuedBook_whenReturnBook_thenThrowBookNotIssuedException() {
        Book book = createBook();
        User user = createUser();
        user.setBooks(new HashSet<>());
        when(bookRepository.findById(BookTestData.ID)).thenReturn(Optional.of(book));
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        assertThatExceptionOfType(BookNotIssuedException.class)
                .isThrownBy(() -> userService.returnBook(ID, BookTestData.ID));
        verify(bookRepository).findById(BookTestData.ID);
        verify(userRepository).findById(ID);
        verify(bookRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void givenValidUserAndBook_whenReturnBook_thenReturnUpdatedUserDto() {
        Book book = createBook();
        User user = createUser();
        user.setBooks(new HashSet<>(List.of(book)));
        Book updatedBook = createBook();
        updatedBook.setAvailable(updatedBook.getAvailable() + 1);
        User updatedUser = createUser();
        updatedUser.setBooks(new HashSet<>());

        UserDto expected = createUserDto();
        expected.setBooks(new HashSet<>());

        when(bookRepository.findById(BookTestData.ID)).thenReturn(Optional.of(book));
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        UserDto actual = userService.returnBook(ID, BookTestData.ID);

        assertThat(actual, is(expected));
        verify(bookRepository).findById(BookTestData.ID);
        verify(userRepository).findById(ID);
        verify(bookRepository).save(updatedBook);
        verify(userRepository).save(updatedUser);
    }
}
