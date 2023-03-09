package util;

import com.my.library.dto.BookDto;

import java.util.ArrayList;
import java.util.List;

public class BookTestData {
    public static final Long ID = 1L;
    public static final Long INVALID_ID = 0L;
    public static final String AUTHOR = "test author";
    public static final String NAME = "test name";
    public static final Integer AVAILABLE = 100;

    public static BookDto createBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(ID);
        bookDto.setAuthor(AUTHOR);
        bookDto.setName(NAME);
        bookDto.setAvailable(AVAILABLE);
        return bookDto;
    }

    public static List<BookDto> createBookDtoList() {
        List<BookDto> bookDtoList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            BookDto bookDto = new BookDto();
            bookDto.setId(ID + i);
            bookDto.setAuthor(AUTHOR + "a".repeat(i));
            bookDto.setName(NAME + "a".repeat(i));
            bookDto.setAvailable(AVAILABLE + i);
            bookDtoList.add(bookDto);
        }
        return bookDtoList;
    }
}
