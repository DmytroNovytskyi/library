package com.my.library.controller.api;

import com.my.library.dto.BookDto;
import com.my.library.validation.group.OnCreate;
import com.my.library.validation.group.OnUpdate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequestMapping("/book")
public interface BookApi {
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    BookDto getById(@PathVariable("id") @Min(value = 1, message = "{bookApi.getById.id.min}") long id);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<BookDto> getSortedPage(@RequestParam(value = "page", defaultValue = "0")
                                @Min(value = 0, message = "{bookApi.getSortedPage.page.min}") int page,
                                @RequestParam(value = "size", defaultValue = "1")
                                @Min(value = 1, message = "{bookApi.getSortedPage.size.min}") int size,
                                @RequestParam(value = "sortBy", defaultValue = "id")
                                @Pattern(regexp = "id|author|name",
                                        message = "{bookApi.getSortedPage.sortBy.pattern}") String sortBy,
                                @RequestParam(value = "order", defaultValue = "asc")
                                @Pattern(regexp = "asc|desc",
                                        message = "{bookApi.getSortedPage.order.pattern}") String order);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    BookDto create(@RequestBody @Validated(OnCreate.class) BookDto book);

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    BookDto update(@RequestBody @Validated(OnUpdate.class) BookDto book);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable("id")
                                @Min(value = 1, message = "{bookApi.delete.id.min}") long id);
}
