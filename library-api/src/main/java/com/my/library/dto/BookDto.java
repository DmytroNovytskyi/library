package com.my.library.dto;

import lombok.Data;

@Data
public class BookDto {
    private Long id;
    private String author;
    private String name;
    private Integer available;
}
