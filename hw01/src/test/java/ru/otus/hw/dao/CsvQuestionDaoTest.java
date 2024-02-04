package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CsvQuestionDaoTest {

    private static final String VALID_CSV_PATH = "questions.csv";
    private static final String NOT_VALID_CSV_PATH = "not_existing_file.csv";

    @Test
    void csvQuestionDao_should_parsCsvFileWithQuestions_whenCsvIsValidFile() {
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(new AppProperties(VALID_CSV_PATH));
        List<Question> questions = csvQuestionDao.findAll();
        assert(questions.size() == 5);
        assert(questions.get(0).text().equals("How many planets are there in the solar system?"));
    }

    @Test
    void csvQuestionDao_should_throwQuestionReadException_whenCsvIsNotValidFile() {
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(new AppProperties(NOT_VALID_CSV_PATH));
        assertThrows(QuestionReadException.class, csvQuestionDao::findAll);
    }
}
