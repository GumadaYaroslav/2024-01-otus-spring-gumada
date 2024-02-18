package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class TestServiceImplTest {

    private static final Question testQuestion = new Question("I you inside unit-test?", new Answer("yes"));

    public static final Student testStudent = new Student("ivan", "Ivanov");
    @Test
    void TestServiceImpl_should_returnValidTestResult() {
        IOService ioService = Mockito.mock(IOService.class);
        Mockito.doNothing().when(ioService).printLine(any());
        Mockito.when(ioService.readStringWithPrompt(testQuestion.text())).thenReturn(testQuestion.answer().text());
        QuestionDao questionDao = Mockito.mock(QuestionDao.class);
        Mockito.when(questionDao.findAll()).thenReturn(List.of(testQuestion));
        TestServiceImpl testService = new TestServiceImpl(ioService, questionDao);
        TestResult testResult = testService.executeTestFor(testStudent);
        assert(testResult.getRightAnswersCount() == 1);
        assert((long) testResult.getAnsweredQuestions().size() == 1);
    }

}