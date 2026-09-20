package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Wordle {
    private static final String DICTIONARY_FILE = "words_ru.txt";
    private static final String LOG_FILE = "wordle.log";

    public static void main(String[] args) {
        try (PrintWriter log = createLog()) {
            try {
                play(log);
            } catch (WordleProgramException e) {
                log.println("Ошибка программы: " + e.getMessage());
                e.printStackTrace(log);
                System.out.println("Ошибка программы, подробности в файле " + LOG_FILE);
            } catch (Exception e) {
                log.println("Непредвиденная ошибка: " + e.getMessage());
                e.printStackTrace(log);
                System.out.println("Непредвиденная ошибка, подробности в файле " + LOG_FILE);
            }
        } catch (LogFileException e) {
            System.out.println(e.getMessage());
        }
    }

    private static PrintWriter createLog() throws LogFileException {
        try {
            return new PrintWriter(new FileWriter(LOG_FILE, StandardCharsets.UTF_8), true);
        } catch (IOException e) {
            throw new LogFileException("Невозможно создать лог-файл " + LOG_FILE, e);
        }
    }

    private static void play(PrintWriter log) throws WordleProgramException {
        WordleDictionaryLoader loader = new WordleDictionaryLoader(log);
        WordleDictionary dictionary = loader.load(DICTIONARY_FILE);
        WordleGame game = new WordleGame(dictionary, log);

        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        System.out.println("Игра Wordle. Угадайте слово из пяти букв. Enter на пустой строке - подсказка.");

        while (!game.isFinished()) {
            System.out.println("Осталось попыток: " + game.getSteps());
            if (!scanner.hasNextLine()) {
                log.println("Ввод завершён до окончания игры");
                break;
            }
            String line = scanner.nextLine();

            try {
                String word = line;
                if (line.trim().isEmpty()) {
                    word = game.getSuggestion();
                    System.out.println("> " + word);
                }
                System.out.println(game.makeMove(word));
            } catch (WordNotFoundInDictionary e) {
                log.println(e.getMessage());
                System.out.println("Такого слова нет в словаре, попробуйте другое.");
            } catch (IncorrectWordException e) {
                log.println(e.getMessage());
                System.out.println("Введите слово из пяти русских букв.");
            }
        }

        if (game.isWon()) {
            System.out.println("Вы выиграли!");
        } else if (game.isFinished()) {
            System.out.println("Вы проиграли.");
        }
        System.out.println("Загаданное слово: " + game.getAnswer());
    }
}