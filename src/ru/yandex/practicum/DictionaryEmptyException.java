package ru.yandex.practicum;

public class DictionaryEmptyException extends WordleProgramException {
    public DictionaryEmptyException(String message) {
        super(message);
    }
}