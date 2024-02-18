package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.StudentDao;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final StudentDao studentDao;

    private final ResultService resultService;

    @Override
    public void run() {
        var student = studentDao.getInfo();
        var testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
    }
}
