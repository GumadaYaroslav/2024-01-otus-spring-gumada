package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.UserNameInfoDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.UserNameInfo;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final IOService ioService;

    private final CsvQuestionDao csvQuestionDao;

    private final UserNameInfoDao userNameInfoDao;

    @Override
    public void executeTest() {
        UserNameInfo nameInfo = userNameInfoDao.getInfo();
        ioService.printLine("Lets start the test mister/misses %s %s".formatted(
                nameInfo.firstName(),
                nameInfo.secondName()));
        for (Question question : csvQuestionDao.findAll()) {
            ioService.printLine(question.text());
        }
    }
}
