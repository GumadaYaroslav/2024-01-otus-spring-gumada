package ru.otus.hw.dao.dto;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import ru.otus.hw.domain.Question;


@Data
public class QuestionDto {

    @CsvBindByPosition(position = 0)
    private String text;

    public Question toDomainObject() {
        return new Question(text);
    }
}
