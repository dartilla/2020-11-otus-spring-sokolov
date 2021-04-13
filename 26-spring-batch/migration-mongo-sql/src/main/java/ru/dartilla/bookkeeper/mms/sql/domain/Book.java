package ru.dartilla.bookkeeper.mms.sql.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import javax.persistence.*;

/**
 * Книга (конкретный экземпляр)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
@NamedEntityGraph(name = "bookEntityGraphWithScriptAndAuthor",
        attributeNodes = {@NamedAttributeNode(value = "script", subgraph = "scriptWithAuthor")},
        subgraphs = {@NamedSubgraph(name = "scriptWithAuthor", attributeNodes = @NamedAttributeNode("author"))})
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

    @Column(name = "mongo_id")
    private String mongoId;
}
