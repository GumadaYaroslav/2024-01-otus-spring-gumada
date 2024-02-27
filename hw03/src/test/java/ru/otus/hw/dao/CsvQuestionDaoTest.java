package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CsvQuestionDaoTest {

    private static final String VALID_CSV_PATH = "test_questions.csv";
    private static final String NOT_VALID_CSV_PATH = "not_existing_file.csv";

    @Test
    void csvQuestionDao_should_parsCsvFileWithQuestions_whenCsvIsValidFile() {
        TestFileNameProvider testFileNameProvider = Mockito.mock(TestFileNameProvider.class);
        Mockito.when(testFileNameProvider.getTestFileName()).thenReturn(VALID_CSV_PATH);
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(testFileNameProvider);
        List<Question> questions = csvQuestionDao.findAll();
        assertEquals(questions.size(), 5);
        assertEquals(questions.get(0).text(), "How many planets are there in the solar system?");
    }

    @Test
    void csvQuestionDao_should_throwQuestionReadException_whenCsvIsNotValidFile() {
        TestFileNameProvider testFileNameProvider = Mockito.mock(TestFileNameProvider.class);
        Mockito.when(testFileNameProvider.getTestFileName()).thenReturn(NOT_VALID_CSV_PATH);
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(testFileNameProvider);
        assertThrows(QuestionReadException.class, csvQuestionDao::findAll);
    }
}
