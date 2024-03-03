package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.CommandScan;
import ru.otus.hw.service.TestRunnerService;

@SpringBootApplication
@CommandScan
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}