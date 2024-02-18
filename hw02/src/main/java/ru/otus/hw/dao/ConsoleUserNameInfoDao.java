package ru.otus.hw.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.UserNameInfo;
import ru.otus.hw.service.IOService;

@Component
@RequiredArgsConstructor
public class ConsoleUserNameInfoDao implements UserNameInfoDao {

    private final IOService ioService;

    private static final String HELLO_USER = "Hollow user!";
    private static final String ENTER_FIRST_NAME = "Enter first name:";
    private static final String ENTER_SECOND_NAME = "Enter second name:";

    @Override
    public UserNameInfo getInfo() {
        ioService.printLine(HELLO_USER);
        String firstName = ioService.scanWithLegend(ENTER_FIRST_NAME);
        String secondName = ioService.scanWithLegend(ENTER_SECOND_NAME);

        return new UserNameInfo(firstName, secondName);
    }
}
