package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {
    private static WordleDictionary dictionary;

    @BeforeAll
    static void setUp() {
        dictionary = new WordleDictionary(List.of("герой", "гонец", "мираж"));
    }

    @Test
    void normalizeLowersCaseAndReplacesYo() {
        assertEquals("елка", WordleDictionary.normalize("  ЁлКа "));
    }

    @Test
    void normalizeNullGivesEmptyString() {
        assertEquals("", WordleDictionary.normalize(null));
    }

    @Test
    void validWordHasFiveRussianLetters() {
        assertTrue(WordleDictionary.isValidWord("герой"));
    }

    @Test
    void invalidWords() {
        assertFalse(WordleDictionary.isValidWord(null));
        assertFalse(WordleDictionary.isValidWord(""));
        assertFalse(WordleDictionary.isValidWord("гер"));
        assertFalse(WordleDictionary.isValidWord("героев"));
        assertFalse(WordleDictionary.isValidWord("hello"));
        assertFalse(WordleDictionary.isValidWord("гер0й"));
    }

    @Test
    void containsWord() {
        assertTrue(dictionary.contains("мираж"));
        assertFalse(dictionary.contains("кошка"));
    }

    @Test
    void randomWordIsFromDictionary() {
        for (int i = 0; i < 20; i++) {
            assertTrue(dictionary.contains(dictionary.getRandomWord()));
        }
    }

    @Test
    void sizeAndWords() {
        assertEquals(3, dictionary.size());
        assertEquals(3, dictionary.getWords().size());
    }

    @Test
    void emptyDictionaryIsNotAllowed() {
        assertThrows(IllegalArgumentException.class, () -> new WordleDictionary(List.of()));
    }
}