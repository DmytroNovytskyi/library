package com.my.library.exception.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ExceptionDetails {
    private String message;
    private ExceptionType exceptionType;
    private LocalDateTime timeStamp;
}
