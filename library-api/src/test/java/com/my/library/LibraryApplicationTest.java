package com.my.library;

import com.my.library.dto.BookDto;
import com.my.library.dto.UserDto;
import com.my.library.util.PaginatedResponse;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.List;
import java.util.Objects;

import static com.my.library.util.BookTestData.createBookDto;
import static com.my.library.util.CommonTestData.*;
import static com.my.library.util.UserTestData.PASSWORD;
import static com.my.library.util.UserTestData.createUserDto;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LibraryApplicationTest {

    private final String paginationParameters = "?page=" + PAGE + "&size=" + SIZE
            + "&sortBy=" + SORT_BY + "&order=" + ORDER;
    @Autowired
    private TestRestTemplate restTemplate;
    @Value("http://localhost:${local.server.port}/")
    private String baseUrl;

    @BeforeTestClass
    public void setup() {
        HttpClient httpClient = HttpClientBuilder.create().build();
        restTemplate.getRestTemplate()
                .setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    @Test
    void givenValidBookId_whenGetById_thenReturnBookDto() {
        long bookId = 1001L;

        ResponseEntity<BookDto> response = restTemplate
                .exchange(baseUrl + "book/" + bookId,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        BookDto.class);
        BookDto returnedBook = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(returnedBook, notNullValue());
        assertThat(returnedBook.getId(), is(bookId));
    }

    @Test
    void givenValidUserId_whenGetById_thenReturnUserDto() {
        long userId = 1001L;

        ResponseEntity<UserDto> response = restTemplate
                .exchange(baseUrl + "user/" + userId,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        UserDto.class);
        UserDto returnedUser = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(returnedUser, notNullValue());
        assertThat(returnedUser.getId(), is(userId));
    }

    @Test
    void givenPageableData_whenGetSortedPage_thenReturnPageOfBooks() {
        ResponseEntity<PaginatedResponse<BookDto>> response = restTemplate
                .exchange(baseUrl + "book" + paginationParameters,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<>() {
                        });
        Page<BookDto> bookDtoPage = response.getBody();
        List<String> bookDtoNameList = Objects.requireNonNull(bookDtoPage).getContent()
                .stream().map(BookDto::getName).toList();

        assertThat(bookDtoPage, notNullValue());
        assertThat(bookDtoPage.getSize(), is(SIZE));
        assertThat(bookDtoPage.getNumber(), is(PAGE));
        System.out.println(bookDtoNameList);
        assertThat(bookDtoNameList, containsInRelativeOrder("Name1", "Name2"));
    }

    @Test
    void givenPageableData_whenGetSortedPage_thenReturnPageOfUsers() {
        ResponseEntity<PaginatedResponse<UserDto>> response = restTemplate
                .exchange(baseUrl + "user" + paginationParameters,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<>() {
                        });
        Page<UserDto> userDtoPage = response.getBody();
        List<String> bookDtoNameList = Objects.requireNonNull(userDtoPage).getContent()
                .stream().map(UserDto::getUsername).toList();

        assertThat(userDtoPage, notNullValue());
        assertThat(userDtoPage.getSize(), is(SIZE));
        assertThat(userDtoPage.getNumber(), is(PAGE));
        assertThat(bookDtoNameList, containsInRelativeOrder("username1", "username2"));
    }

    @Test
    void givenValidBookDto_whenCreate_thenReturnCreatedBookDto() {
        BookDto bookDto = createBookDto();
        bookDto.setId(null);

        ResponseEntity<BookDto> response = restTemplate
                .postForEntity(baseUrl + "book", bookDto, BookDto.class);
        BookDto returnedBookDto = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(returnedBookDto, notNullValue());
        assertThat(returnedBookDto.getId(), notNullValue());
        assertThat(returnedBookDto.getAuthor(), is(bookDto.getAuthor()));
        assertThat(returnedBookDto.getName(), is(bookDto.getName()));
        assertThat(returnedBookDto.getAvailable(), is(bookDto.getAvailable()));
    }

    @Test
    void givenValidUserDto_whenCreate_thenReturnCreatedUserDto() {
        UserDto userDto = createUserDto();
        userDto.setId(null);
        userDto.setPassword(PASSWORD);

        ResponseEntity<UserDto> response = restTemplate
                .postForEntity(baseUrl + "user", userDto, UserDto.class);
        UserDto returnedUserDto = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(returnedUserDto, notNullValue());
        assertThat(returnedUserDto.getId(), notNullValue());
        assertThat(returnedUserDto.getUsername(), is(userDto.getUsername()));
        assertThat(returnedUserDto.getEmail(), is(userDto.getEmail()));
        assertThat(returnedUserDto.getPassword(), nullValue());
        assertThat(returnedUserDto.getBooks(), nullValue());
    }

    @Test
    void givenValidBookDto_whenUpdate_thenReturnUpdatedBookDto() {
        long bookId = 1001L;
        String updatedName = "newName1";
        BookDto bookDto = new BookDto();
        bookDto.setId(bookId);
        bookDto.setName(updatedName);
        HttpEntity<BookDto> requestEntity = new HttpEntity<>(bookDto);

        ResponseEntity<BookDto> response = restTemplate
                .exchange(baseUrl + "book", HttpMethod.PATCH, requestEntity, BookDto.class);
        BookDto returnedBookDto = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(returnedBookDto, notNullValue());
        assertThat(returnedBookDto.getId(), is(bookId));
        assertThat(returnedBookDto.getName(), is(updatedName));
    }

    @Test
    void givenValidUserDto_whenUpdate_thenReturnUpdatedUserDto() {
        long userId = 1001L;
        String updatedEmail = "new_email1@gmail.com";
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setEmail(updatedEmail);
        HttpEntity<UserDto> requestEntity = new HttpEntity<>(userDto);

        ResponseEntity<UserDto> response = restTemplate
                .exchange(baseUrl + "user", HttpMethod.PATCH, requestEntity, UserDto.class);
        System.out.println(response);
        UserDto returnedUserDto = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(returnedUserDto, notNullValue());
        assertThat(returnedUserDto.getId(), is(userId));
        assertThat(returnedUserDto.getEmail(), is(updatedEmail));
    }

    @Test
    void givenValidBookId_whenDelete_thenDeleteAndReturnNothing() {
        long bookId = 1001L;

        ResponseEntity<Void> response = restTemplate
                .exchange(baseUrl + "book/" + bookId, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);

        assertThat(response.getStatusCode(), is(HttpStatus.NO_CONTENT));
        assertThat(response.getBody(), nullValue());
    }

    @Test
    void givenValidUserId_whenDelete_thenDeleteAndReturnNothing() {
        long userId = 1001L;

        ResponseEntity<Void> response = restTemplate
                .exchange(baseUrl + "user/" + userId, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);

        assertThat(response.getStatusCode(), is(HttpStatus.NO_CONTENT));
        assertThat(response.getBody(), nullValue());
    }

    @Test
    void givenValidUserAndBookId_whenIssueBook_thenReturnUserWithIssuedBook() {
        long bookId = 1001L;
        long userId = 1001L;

        ResponseEntity<UserDto> response = restTemplate
                .exchange(baseUrl + "user/issue/" + userId + "/" + bookId,
                        HttpMethod.POST, HttpEntity.EMPTY, UserDto.class);
        UserDto returnedUserDto = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(returnedUserDto, notNullValue());
        assertThat(returnedUserDto.getId(), is(userId));
        assertThat(returnedUserDto.getBooks(), notNullValue());
        assertThat(returnedUserDto.getBooks().isEmpty(), is(false));
        assertThat(returnedUserDto.getBooks().iterator().next().getId(), is(bookId));
    }

    @Test
    void givenValidUserAndBookId_whenReturnBook_thenReturnUserWithEmptyBooks() {
        long bookId = 1002L;
        long userId = 1002L;

        ResponseEntity<UserDto> response = restTemplate
                .exchange(baseUrl + "user/return/" + userId + "/" + bookId,
                        HttpMethod.POST, HttpEntity.EMPTY, UserDto.class);
        UserDto returnedUserDto = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(returnedUserDto, notNullValue());
        assertThat(returnedUserDto.getId(), is(userId));
        assertThat(returnedUserDto.getBooks(), notNullValue());
        assertThat(returnedUserDto.getBooks().isEmpty(), is(true));
    }
}
