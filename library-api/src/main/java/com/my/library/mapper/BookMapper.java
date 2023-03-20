package com.my.library.mapper;

import com.my.library.dto.BookDto;
import com.my.library.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * A mapper interface for converting between Book and BookDto objects.
 * Also allows to map present fields for Book objects.
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {

    /**
     * An instance of the mapper.
     */
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    /**
     * Converts a Book object to a BookDto object.
     *
     * @param book The Book object to convert
     * @return The corresponding BookDto object
     */
    BookDto mapBookDto(Book book);

    /**
     * Converts a BookDto object to a Book object.
     *
     * @param bookDto The BookDto object to convert
     * @return The corresponding Book object
     */
    Book mapBook(BookDto bookDto);

    /**
     * Copies the present fields of a Book object from another Book object.
     *
     * @param toBook   The Book object to copy the fields to
     * @param fromBook The Book object to copy the fields from
     */
    void mapPresentFields(@MappingTarget Book toBook, Book fromBook);
}
