package ru.javawebinar.voting.util.exception;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {

    private final String message;
    private final HttpStatus httpStatus;

    public ApplicationException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
