package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Component
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    public CsvQuestionDao(@Autowired TestFileNameProvider fileNameProvider) {
        this.fileNameProvider = fileNameProvider;
    }

    @Override
    public List<Question> findAll() {
        try {
            InputStream questionsCsvStream = getClass().getClassLoader()
                    .getResourceAsStream(fileNameProvider.getTestFileName());
            Reader questionsFR = new InputStreamReader(questionsCsvStream);
            CsvToBean<QuestionDto> build = new CsvToBeanBuilder(questionsFR)
                    .withType(QuestionDto.class)
                    .withSeparator(';')
                    .withSkipLines(1).build();

            return build.stream().map(QuestionDto::toDomainObject).toList();
        } catch (Exception e) {
            throw new QuestionReadException("Error wile reading questions", e);
        }
    }
}
