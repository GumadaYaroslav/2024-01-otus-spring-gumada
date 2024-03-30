package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = CsvQuestionDao.class)
public class CsvQuestionDaoTest {

    private static final String VALID_CSV_PATH = "test_questions.csv";
    private static final String NOT_VALID_CSV_PATH = "not_existing_file.csv";

    @MockBean
    TestFileNameProvider testFileNameProvider;

    @Autowired
    CsvQuestionDao csvQuestionDao;

    @Test
    void csvQuestionDao_should_parsCsvFileWithQuestions_whenCsvIsValidFile() {
        Mockito.when(testFileNameProvider.getTestFileName()).thenReturn(VALID_CSV_PATH);
        List<Question> questions = csvQuestionDao.findAll();
        assertEquals(questions.size(), 5);
        assertEquals(questions.get(0).text(), "How many planets are there in the solar system?");
    }

    @Test
    void csvQuestionDao_should_throwQuestionReadException_whenCsvIsNotValidFile() {
        Mockito.when(testFileNameProvider.getTestFileName()).thenReturn(NOT_VALID_CSV_PATH);
        assertThrows(QuestionReadException.class, csvQuestionDao::findAll);
    }
}
