package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;

@Command(group = "Run Test Command")
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    @Command(description = "start test", command = "run", alias = "r")
    public void run() {
        var student = studentService.determineCurrentStudent();
        var testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
    }
}
