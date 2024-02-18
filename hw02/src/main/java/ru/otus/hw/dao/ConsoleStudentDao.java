package ru.otus.hw.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.IOService;

@Component
@RequiredArgsConstructor
public class ConsoleStudentDao implements StudentDao {

    private final IOService ioService;

    private static final String HELLO_USER = "Hollow user!";
    private static final String ENTER_FIRST_NAME = "Enter first name:";
    private static final String ENTER_SECOND_NAME = "Enter last name:";

    @Override
    public Student getInfo() {
        ioService.printLine(HELLO_USER);
        String firstName = ioService.readStringWithPrompt(ENTER_FIRST_NAME);
        String lastName = ioService.readStringWithPrompt(ENTER_SECOND_NAME);

        return new Student(firstName, lastName);
    }
}
