package ru.otus.hw.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
// todo создать бин
public class AppProperties implements TestFileNameProvider {
    private String testFileName;
}
