package ru.dartilla.bookkeeper.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "in_storage", columnDefinition = "INTEGER")
    private boolean inStorage;

    @ManyToOne
    @BatchSize(size = 950)
    @JoinColumn(name = "script_id")
    private Script script;
}
