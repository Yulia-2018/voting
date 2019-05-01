package ru.javawebinar.voting.util.exception;

import org.springframework.http.HttpStatus;

public class InvalidDateTimeException extends ApplicationException {
    public InvalidDateTimeException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
