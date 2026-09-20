package ru.yandex.practicum;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class WordleDictionary {
    public static final int WORD_LENGTH = 5;

    private final List<String> words;
    private final Set<String> wordSet;
    private final Random random = new Random();

    public WordleDictionary(List<String> words) {
        if (words == null || words.isEmpty()) {
            throw new IllegalArgumentException("Словарь не может быть пустым");
        }
        this.words = List.copyOf(words);
        this.wordSet = new HashSet<>(words);
    }

    public static String normalize(String word) {
        if (word == null) {
            return "";
        }
        return word.trim().toLowerCase(Locale.ROOT).replace('ё', 'е');
    }

    public static boolean isValidWord(String word) {
        if (word == null || word.length() != WORD_LENGTH) {
            return false;
        }
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (c < 'а' || c > 'я') {
                return false;
            }
        }
        return true;
    }

    public boolean contains(String word) {
        return wordSet.contains(word);
    }

    public String getRandomWord() {
        return words.get(random.nextInt(words.size()));
    }

    public List<String> getWords() {
        return words;
    }

    public int size() {
        return words.size();
    }
}