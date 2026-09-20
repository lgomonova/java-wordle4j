package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class WordleDictionaryLoader {
    private final PrintWriter log;

    public WordleDictionaryLoader(PrintWriter log) {
        this.log = log;
    }

    public WordleDictionary load(String fileName) throws WordleProgramException {
        Set<String> words = new LinkedHashSet<>();
        int total = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                total++;
                String word = WordleDictionary.normalize(line);
                if (WordleDictionary.isValidWord(word)) {
                    words.add(word);
                }
            }
        } catch (FileNotFoundException e) {
            throw new DictionaryNotFoundException("Не найден файл словаря: " + fileName, e);
        } catch (IOException e) {
            throw new WordleProgramException("Ошибка чтения файла словаря: " + fileName, e);
        }

        log.println("Прочитано строк словаря: " + total + ", подходящих слов: " + words.size());

        if (words.isEmpty()) {
            throw new DictionaryEmptyException("В словаре " + fileName + " нет слов из пяти букв");
        }
        return new WordleDictionary(new ArrayList<>(words));
    }
}