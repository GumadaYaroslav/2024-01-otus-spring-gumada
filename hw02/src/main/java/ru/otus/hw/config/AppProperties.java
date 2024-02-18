package ru.otus.hw.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
public class AppProperties implements TestFileNameProvider {
    private final String testFileName;

    public AppProperties(@Value("${app.testFileName}") String testFileName) {
        this.testFileName = testFileName;
    }
    @Override
    public String getTestFileName() {
        return testFileName;
    }
}
