package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    private static WordleDictionary dictionary;
    private static PrintWriter log;
    private WordleGame game;

    @BeforeAll
    static void setUpAll() {
        dictionary = new WordleDictionary(List.of("герой", "гонец", "мираж", "ворон", "норов", "шалаш", "аллея"));
        log = new PrintWriter(System.out);
    }

    @BeforeEach
    void setUp() {
        game = new WordleGame(dictionary, log, "герой");
    }

    @Test
    void hintFromTaskExample() {
        assertEquals("+^-^-", WordleGame.computeHint("гонец", "герой"));
    }

    @Test
    void hintForFullMatch() {
        assertEquals("+++++", WordleGame.computeHint("герой", "герой"));
    }

    @Test
    void hintWithRepeatedLettersInGuess() {
        assertEquals("^-+--", WordleGame.computeHint("аллея", "шалаш"));
    }

    @Test
    void hintWithRepeatedLettersInAnswer() {
        assertEquals("^+++^", WordleGame.computeHint("ворон", "норов"));
    }

    @Test
    void repeatedLetterIsMarkedOnlyAsManyTimesAsInAnswer() {
        assertEquals("--++-", WordleGame.computeHint("ворон", "герой"));
    }

    @Test
    void newGameState() {
        assertEquals(6, game.getSteps());
        assertFalse(game.isFinished());
        assertFalse(game.isWon());
        assertEquals("герой", game.getAnswer());
    }

    @Test
    void moveDecreasesSteps() throws Exception {
        assertEquals("+^-^-", game.makeMove("гонец"));
        assertEquals(5, game.getSteps());
    }

    @Test
    void moveIsNormalized() throws Exception {
        game.makeMove("  ГОНЕЦ ");
        assertEquals(5, game.getSteps());
    }

    @Test
    void correctAnswerWinsGame() throws Exception {
        assertEquals("+++++", game.makeMove("герой"));
        assertTrue(game.isWon());
        assertTrue(game.isFinished());
    }

    @Test
    void wordNotInDictionaryIsNotCounted() {
        assertThrows(WordNotFoundInDictionary.class, () -> game.makeMove("кошка"));
        assertEquals(6, game.getSteps());
    }

    @Test
    void incorrectWordsAreNotCounted() {
        assertThrows(IncorrectWordException.class, () -> game.makeMove(""));
        assertThrows(IncorrectWordException.class, () -> game.makeMove("кот"));
        assertThrows(IncorrectWordException.class, () -> game.makeMove("hello"));
        assertEquals(6, game.getSteps());
    }

    @Test
    void gameIsLostWhenStepsRunOut() throws Exception {
        for (int i = 0; i < WordleGame.MAX_STEPS; i++) {
            game.makeMove("гонец");
        }
        assertEquals(0, game.getSteps());
        assertTrue(game.isFinished());
        assertFalse(game.isWon());
    }

    @Test
    void moveAfterGameEndThrows() throws Exception {
        game.makeMove("герой");
        assertThrows(IllegalStateException.class, () -> game.makeMove("гонец"));
    }

    @Test
    void answerMustBeInDictionary() {
        assertThrows(IllegalArgumentException.class, () -> new WordleGame(dictionary, log, "кошка"));
    }

    @Test
    void suggestionWithoutMovesIsFromDictionary() {
        assertTrue(dictionary.contains(game.getSuggestion()));
    }

    @Test
    void suggestionsNeverLoseAnswerAndNeverRepeat() throws Exception {
        for (int i = 0; i < 20; i++) {
            WordleGame g = new WordleGame(dictionary, log);
            int moves = 0;
            while (!g.isFinished()) {
                g.makeMove(g.getSuggestion());
                moves++;
                assertTrue(moves <= WordleGame.MAX_STEPS);
            }
            assertTrue(g.isWon());
        }
    }

    @Test
    void suggestionMatchesPreviousHints() throws Exception {
        game.makeMove("гонец");
        String suggestion = game.getSuggestion();
        assertNotEquals("гонец", suggestion);
        assertEquals("+^-^-", WordleGame.computeHint("гонец", suggestion));
    }

    @Test
    void suggestionAfterGameEndThrows() throws Exception {
        game.makeMove("герой");
        assertThrows(IllegalStateException.class, () -> game.getSuggestion());
    }
}