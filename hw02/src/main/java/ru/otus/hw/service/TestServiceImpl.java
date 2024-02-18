package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Question;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final IOService ioService;

    private final CsvQuestionDao csvQuestionDao;

    @Override
    public void executeTest() {
        for (Question question : csvQuestionDao.findAll()) {
            ioService.printLine(question.text());
        }
    }
}
