package ru.javawebinar.voting.util.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApplicationException {
    public NotFoundException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
