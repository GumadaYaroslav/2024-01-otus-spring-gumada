package ru.otus.hw.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppProperties implements TestFileNameProvider, TestConfig {
    private final String testFileName;

    public AppProperties(@Value("${test.fileName}") String testFileName,
                         @Value("${test.rightAnswersCountToPass}") int rightAnswersCountToPass) {
        this.testFileName = testFileName;
    }

    @Override
    public String getTestFileName() {
        return testFileName;
    }

    @Override
    public int getRightAnswersCountToPass() {
        return 0;
    }
}
