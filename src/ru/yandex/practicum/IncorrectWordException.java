package ru.yandex.practicum;

public class IncorrectWordException extends WordleGameException {
    public IncorrectWordException(String message) {
        super(message);
    }
}