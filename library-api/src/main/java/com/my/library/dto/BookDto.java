package com.my.library.dto;

import com.my.library.validation.group.OnCreate;
import com.my.library.validation.group.OnUpdate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

@Data
public class BookDto {
    @Null(message = "{book.id.null}", groups = OnCreate.class)
    @NotNull(message = "{book.id.notNull}", groups = OnUpdate.class)
    private Long id;

    @NotNull(message = "{book.author.notNull}", groups = OnCreate.class)
    private String author;

    @NotNull(message = "{book.name.notNull}", groups = OnCreate.class)
    private String name;

    @Null(message = "{book.available.null}", groups = OnUpdate.class)
    @NotNull(message = "{book.available.notNull}", groups = OnCreate.class)
    @Min(value = 1, message = "{book.available.min}", groups = OnCreate.class)
    private Integer available;
}
