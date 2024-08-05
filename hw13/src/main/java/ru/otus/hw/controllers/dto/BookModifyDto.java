package ru.otus.hw.controllers.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



import jakarta.validation.constraints.Pattern;
import ru.otus.hw.models.Book;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookModifyDto {
    private Long id;

    @Pattern.List({
            @Pattern(regexp = "^(?!title$)(.+)", message = "Change default value")
    })
    @Size(min = 2, message = "input more then 2 chars")
    private String title;

    @Min(value = 1, message = "Author id=0, not valid id")
    private long authorId;

    @Min(value = 1, message = "Genre id=0, not valid id ")
    private long genreId;


    public static BookModifyDto toDto(Book book) {
        return new BookModifyDto(book.getId(),
                book.getTitle(),
                book.getAuthor().getId(),
                book.getGenre().getId()
        );
    }
}