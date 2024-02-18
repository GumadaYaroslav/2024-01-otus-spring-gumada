package ru.otus.hw.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class StreamsIOService implements IOService {
    private final PrintStream printStream;
    private final InputStream inputStream;

    public StreamsIOService(PrintStream printStream, InputStream inputStream) {

        this.printStream = printStream;
        this.inputStream = inputStream;
    }

    @Override
    public void printLine(String s) {
        printStream.println(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        printStream.printf(s + "%n", args);
    }

    @SneakyThrows
    @Override
    public String scanWithLegend(String legend) {
        printStream.println(legend);
        return scan();
    }

    @SneakyThrows
    @Override
    public String scan() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        return reader.readLine();
    }
}
