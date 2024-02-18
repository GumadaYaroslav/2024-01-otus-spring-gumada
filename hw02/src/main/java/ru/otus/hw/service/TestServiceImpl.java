package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.StudentDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final IOService ioService;

    private final CsvQuestionDao csvQuestionDao;

    private final StudentDao studentDao;

    @Override
    public void executeTest() {
        Student nameInfo = studentDao.getInfo();
        ioService.printLine("Lets start the test mister/misses %s %s".formatted(
                nameInfo.firstName(),
                nameInfo.lastName()));
        for (Question question : csvQuestionDao.findAll()) {
            ioService.printLine(question.text());
        }
    }
}
