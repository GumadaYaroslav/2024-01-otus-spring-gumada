package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.io.PrintStream;

import static java.lang.System.in;
import static java.lang.System.out;

@Configuration
public class IoServiceConfig {

    @Bean
    PrintStream printStream() {
        return out;
    }

    @Bean
    InputStream inputStream() {
        return in;
    }
}
