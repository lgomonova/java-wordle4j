package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryLoaderTest {
    private static WordleDictionaryLoader loader;

    @BeforeAll
    static void setUp() {
        loader = new WordleDictionaryLoader(new PrintWriter(System.out));
    }

    private Path createFile(String content) throws IOException {
        Path file = Files.createTempFile("dict", ".txt");
        Files.writeString(file, content, StandardCharsets.UTF_8);
        file.toFile().deleteOnExit();
        return file;
    }

    @Test
    void loadsOnlyFiveLetterWordsInNormalForm() throws Exception {
        Path file = createFile("кот\nГЕРОЙ\nщёлка\nсамолёт\nМираж\nгерой\n");
        WordleDictionary dictionary = loader.load(file.toString());

        assertEquals(3, dictionary.size());
        assertTrue(dictionary.contains("герой"));
        assertTrue(dictionary.contains("щелка"));
        assertTrue(dictionary.contains("мираж"));
    }

    @Test
    void missingFileThrows() {
        assertThrows(DictionaryNotFoundException.class, () -> loader.load("no_such_file.txt"));
    }

    @Test
    void dictionaryWithoutSuitableWordsThrows() throws Exception {
        Path file = createFile("кот\nсамолет\n");
        assertThrows(DictionaryEmptyException.class, () -> loader.load(file.toString()));
    }
}