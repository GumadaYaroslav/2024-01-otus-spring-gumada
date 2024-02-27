package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.domain.Student;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceImplTest {
    public static final String STUDENT_FIRST_NAME = "Ivan";
    public static final String STUDENT_LAST_NAME = "Ivanovich";

    @Test
    void determineCurrentStudent_should_readUserCorrect() {
        LocalizedIOService mockIoService = Mockito.mock(LocalizedIOService.class);
        Mockito.when(mockIoService.readStringWithPromptLocalized("StudentService.input.first.name"))
                .thenReturn(STUDENT_FIRST_NAME);
        Mockito.when(mockIoService.readStringWithPromptLocalized("StudentService.input.last.name"))
                .thenReturn(STUDENT_LAST_NAME);
        StudentServiceImpl studentService = new StudentServiceImpl(mockIoService);
        Student student = studentService.determineCurrentStudent();
        assertEquals(student.firstName(), STUDENT_FIRST_NAME);
        assertEquals(student.lastName(), STUDENT_LAST_NAME);
    }
}