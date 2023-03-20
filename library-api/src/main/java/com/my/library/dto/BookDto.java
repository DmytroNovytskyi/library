package com.my.library.dto;

import com.my.library.validation.group.OnCreate;
import com.my.library.validation.group.OnUpdate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

/**
 * Data transfer object for Book entity.
 */
@Data
public class BookDto {

    /**
     * Book unique identifier. Must be null on create and not null on update.
     */
    @Null(message = "{book.id.null}", groups = OnCreate.class)
    @NotNull(message = "{book.id.notNull}", groups = OnUpdate.class)
    private Long id;

    /**
     * Book author. Must be not null on create.
     */
    @NotNull(message = "{book.author.notNull}", groups = OnCreate.class)
    private String author;

    /**
     * Book name. Must be not null on create.
     */
    @NotNull(message = "{book.name.notNull}", groups = OnCreate.class)
    private String name;

    /**
     * Number of available copies of the book. Must be not null on create and be greater than 0.
     * Must be null on update.
     */
    @Null(message = "{book.available.null}", groups = OnUpdate.class)
    @NotNull(message = "{book.available.notNull}", groups = OnCreate.class)
    @Min(value = 1, message = "{book.available.min}", groups = OnCreate.class)
    private Integer available;
}
