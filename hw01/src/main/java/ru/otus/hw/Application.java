package ru.otus.hw;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.hw.service.TestRunnerServiceImpl;

public class Application {
    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("/spring-context.xml");
        TestRunnerServiceImpl runnerService = context.getBean(TestRunnerServiceImpl.class);
        runnerService.run();
    }
}