package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.domain.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = StudentServiceImpl.class)
class StudentServiceImplTest {
    public static final String STUDENT_FIRST_NAME = "Ivan";
    public static final String STUDENT_LAST_NAME = "Ivanovich";

    @MockBean
    LocalizedIOService localizedIOService;

    @Autowired
    StudentService studentService;

    @Test
    void determineCurrentStudent_should_readUserCorrect() {
        Mockito.when(localizedIOService.readStringWithPromptLocalized("StudentService.input.first.name"))
                .thenReturn(STUDENT_FIRST_NAME);
        Mockito.when(localizedIOService.readStringWithPromptLocalized("StudentService.input.last.name"))
                .thenReturn(STUDENT_LAST_NAME);
        Student student = studentService.determineCurrentStudent();
        assertEquals(student.firstName(), STUDENT_FIRST_NAME);
        assertEquals(student.lastName(), STUDENT_LAST_NAME);
    }
}