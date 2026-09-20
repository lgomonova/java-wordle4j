package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WordleGame {
    public static final int MAX_STEPS = 6;

    private final WordleDictionary dictionary;
    private final PrintWriter log;
    private final String answer;
    private final List<String> guesses = new ArrayList<>();
    private final List<String> hints = new ArrayList<>();
    private final Set<String> guessedSet = new HashSet<>();
    private final Random random = new Random();
    private int steps = MAX_STEPS;
    private boolean won = false;

    public WordleGame(WordleDictionary dictionary, PrintWriter log) {
        this(dictionary, log, dictionary.getRandomWord());
    }

    public WordleGame(WordleDictionary dictionary, PrintWriter log, String answer) {
        if (!dictionary.contains(answer)) {
            throw new IllegalArgumentException("Загаданного слова нет в словаре: " + answer);
        }
        this.dictionary = dictionary;
        this.log = log;
        this.answer = answer;
        log.println("Новая игра, загадано слово: " + answer + ", шагов: " + steps);
    }

    public String makeMove(String input) throws IncorrectWordException, WordNotFoundInDictionary {
        if (isFinished()) {
            throw new IllegalStateException("Игра уже завершена");
        }

        String word = WordleDictionary.normalize(input);
        if (!WordleDictionary.isValidWord(word)) {
            throw new IncorrectWordException("Слово должно состоять из пяти русских букв: " + input);
        }
        if (!dictionary.contains(word)) {
            throw new WordNotFoundInDictionary("Слова нет в словаре: " + word);
        }

        String hint = computeHint(word, answer);
        guesses.add(word);
        guessedSet.add(word);
        hints.add(hint);
        steps--;
        if (word.equals(answer)) {
            won = true;
        }

        if (steps < 0 || guesses.size() != hints.size()) {
            throw new IllegalStateException("Некорректное состояние игры: шагов " + steps
                    + ", слов " + guesses.size() + ", подсказок " + hints.size());
        }
        log.println("Ход: " + word + " -> " + hint + ", осталось шагов: " + steps + ", выиграна: " + won);
        return hint;
    }

    public static String computeHint(String guess, String answer) {
        StringBuilder hint = new StringBuilder("-".repeat(WordleDictionary.WORD_LENGTH));
        int[] remaining = new int[32];

        for (int i = 0; i < WordleDictionary.WORD_LENGTH; i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                hint.setCharAt(i, '+');
            } else {
                remaining[answer.charAt(i) - 'а']++;
            }
        }
        for (int i = 0; i < WordleDictionary.WORD_LENGTH; i++) {
            if (hint.charAt(i) == '+') {
                continue;
            }
            int index = guess.charAt(i) - 'а';
            if (remaining[index] > 0) {
                hint.setCharAt(i, '^');
                remaining[index]--;
            }
        }
        return hint.toString();
    }

    public String getSuggestion() {
        if (isFinished()) {
            throw new IllegalStateException("Игра уже завершена");
        }

        List<String> candidates = new ArrayList<>();
        for (String word : dictionary.getWords()) {
            if (!guessedSet.contains(word) && matchesAll(word)) {
                candidates.add(word);
            }
        }
        log.println("Подходящих слов для подсказки: " + candidates.size());

        if (candidates.isEmpty()) {
            throw new IllegalStateException("Не найдено подходящих слов, решение потеряно");
        }
        return candidates.get(random.nextInt(candidates.size()));
    }

    private boolean matchesAll(String candidate) {
        for (int i = 0; i < guesses.size(); i++) {
            if (!computeHint(guesses.get(i), candidate).equals(hints.get(i))) {
                return false;
            }
        }
        return true;
    }

    public int getSteps() {
        return steps;
    }

    public boolean isWon() {
        return won;
    }

    public boolean isFinished() {
        return won || steps == 0;
    }

    public String getAnswer() {
        return answer;
    }
}