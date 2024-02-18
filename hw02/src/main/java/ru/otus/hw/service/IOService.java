package ru.otus.hw.service;

public interface IOService {
    void printLine(String s);

    void printFormattedLine(String s, Object ...args);

    String scanWithLegend(String legend);

    String scan();
}
