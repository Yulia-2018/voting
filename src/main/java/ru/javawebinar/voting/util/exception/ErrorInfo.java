package ru.javawebinar.voting.util.exception;

public class ErrorInfo {
    private final String url;
    private final String[] details;

    public ErrorInfo(CharSequence url, String... details) {
        this.url = url.toString();
        this.details = details;
    }
}
