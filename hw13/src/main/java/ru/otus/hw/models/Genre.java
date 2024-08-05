package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "genres")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Genre {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    @Getter
    private String name;
}
