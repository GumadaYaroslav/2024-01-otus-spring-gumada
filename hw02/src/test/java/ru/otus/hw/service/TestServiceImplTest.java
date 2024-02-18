package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

class TestServiceImplTest {

    private static final Question TEST_QUESTION = new Question("I you inside unit-test?", new Answer("yes"));

    public static final Student TEST_STUDENT = new Student("ivan", "Ivanov");
    @Test
    void TestServiceImpl_should_returnValidTestResult() {
        IOService ioService = Mockito.mock(IOService.class);
        Mockito.doNothing().when(ioService).printLine(any());
        Mockito.when(ioService.readStringWithPrompt(TEST_QUESTION.text())).thenReturn(TEST_QUESTION.answer().text());
        QuestionDao questionDao = Mockito.mock(QuestionDao.class);
        Mockito.when(questionDao.findAll()).thenReturn(List.of(TEST_QUESTION));
        TestServiceImpl testService = new TestServiceImpl(ioService, questionDao);
        TestResult testResult = testService.executeTestFor(TEST_STUDENT);
        assert(testResult.getRightAnswersCount() == 1);
        assert((long) testResult.getAnsweredQuestions().size() == 1);
    }

}