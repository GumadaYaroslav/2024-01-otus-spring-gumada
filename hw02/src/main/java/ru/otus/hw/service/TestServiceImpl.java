package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Component
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            String answerFromUser = ioService.readStringWithPrompt(question.text());

            var isAnswerValid = false;
            if (question.answer().text().equals(answerFromUser)) {
                isAnswerValid = true;
            }
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
