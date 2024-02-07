package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.*;
import java.net.URL;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        try {
            InputStream questionsCsvStream = getClass().getClassLoader().getResourceAsStream(fileNameProvider.getTestFileName());
            Reader questionsFR = new InputStreamReader(questionsCsvStream);
            CsvToBean<QuestionDto> build = new CsvToBeanBuilder(questionsFR)
                    .withType(QuestionDto.class)
                    .withSkipLines(1).build();

            return build.stream().map(QuestionDto::toDomainObject).toList();
        } catch (Exception e) {
            throw new QuestionReadException("Error wile reading questions", e);
        }
    }
}
