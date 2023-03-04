package com.my.library.mapper;

import com.my.library.dto.BookDto;
import com.my.library.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookDto mapBookDto(Book book);

    Book mapBook(BookDto bookDto);

    void mapPresentFields(@MappingTarget Book toBook, Book fromBook);
}
