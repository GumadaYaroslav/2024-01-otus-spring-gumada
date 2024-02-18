package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.PrintStream;

@Configuration
public class IoServiceConfig {

    @Bean
    PrintStream printStream() {
        return java.lang.System.out;
    }
}
