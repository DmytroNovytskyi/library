package com.my.library.exception.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Class for wrapping exception details including message, type, and timestamp.
 */
@Data
@AllArgsConstructor
public class ExceptionDetails {
    private String message;
    private ExceptionType exceptionType;
    private LocalDateTime timeStamp;
}
