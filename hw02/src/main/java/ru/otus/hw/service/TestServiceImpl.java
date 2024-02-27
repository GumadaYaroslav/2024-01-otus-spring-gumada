package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
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
        ioService.printLine("Please answers the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            ioService.printFormattedLine(createQuestionStringBuilder(question));
            testResult.applyAnswer(question, getRightOrNotAnswer(question));
        }
        return testResult;
    }

        private String createQuestionStringBuilder(Question qst) {
            StringBuilder questionSb = new StringBuilder();
            questionSb.append("Question: " + qst.text() + " %n");
            for (Answer answ : qst.answers()) {
                questionSb.append(qst.answers().indexOf(answ) + ". " + answ.text() + " %n");
            }
            return questionSb.toString();
        }

        private boolean getRightOrNotAnswer(Question question) {
            int max = question.answers().size() - 1;
            int countAnsw = ioService.readIntForRange(0, max, "Enter int value between 0 - " + max);
            return question.answers().get(countAnsw).text();
        }
}
