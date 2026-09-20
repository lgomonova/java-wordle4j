package ru.yandex.practicum;

public class DictionaryNotFoundException extends WordleProgramException {
    public DictionaryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}