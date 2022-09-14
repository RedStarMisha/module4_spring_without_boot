package ru.practicum.item;

public class UnsupportedRequestParametersException extends RuntimeException {

    public UnsupportedRequestParametersException(String message) {
        super(message);
    }
}
