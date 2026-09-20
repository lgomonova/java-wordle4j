package ru.yandex.practicum;

public class WordNotFoundInDictionary extends WordleGameException {
    public WordNotFoundInDictionary(String message) {
        super(message);
    }
}