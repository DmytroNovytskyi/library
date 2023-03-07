package com.my.library.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.my.library.validation.group.OnCreate;
import com.my.library.validation.group.OnUpdate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    @Null(message = "{user.id.null}", groups = OnCreate.class)
    @NotNull(message = "{user.id.notNull}", groups = OnUpdate.class)
    private Long id;

    @Null(message = "{user.username.null}", groups = OnUpdate.class)
    @NotNull(message = "{user.username.notNull}", groups = OnCreate.class)
    @Pattern(message = "{user.username.pattern}", regexp = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,8}[a-zA-Z0-9]$")
    private String username;

    @NotNull(message = "{user.email.notNull}", groups = OnCreate.class)
    @Pattern(message = "{user.email.pattern}",
            regexp = "^(?=[a-zA-Z0-9._@%-]{6,255}$)[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}$")
    private String email;

    @NotNull(message = "{user.password.notNull}", groups = OnCreate.class)
    @Pattern(message = "{user.password.pattern}",
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,32}$")
    private String password;

    @Null(message = "{user.books.null}")
    private Set<BookDto> books;
}
