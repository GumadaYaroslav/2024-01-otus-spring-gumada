package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    public static final String CHOOSE_ANSWER_CODE = "TestService.choose.the.answer";

    public static final String INVALID_ANSWER_CODE = "TestService.choose.invalid.answer";

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            ioService.printFormattedLine(question.text());
            printAnswers(question);
            testResult.applyAnswer(question, askQuestion(question));
        }
        return testResult;
    }

    private boolean askQuestion(Question question) {
        int max = question.answers().size() - 1;
        int answerNumberFromUser = ioService.readIntForRangeWithPromptLocalized(0,
                max,
                CHOOSE_ANSWER_CODE,
                INVALID_ANSWER_CODE);
        return question.answers().get(answerNumberFromUser - 1).isCorrect();
    }

    private void printAnswers(Question question) {
        for (int numberOfQuestion = 1; numberOfQuestion <= question.answers().size(); numberOfQuestion++) {
            ioService.printLine("%d. ".formatted(numberOfQuestion) +
                    question.answers().get(numberOfQuestion - 1).text());
        }
    }

}
