package ru.dartilla.bookkeeper.mms.sql.domain;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.Set;

/**
 * Рукопись (книга как литературное произведение)
 */
@Data
@ToString(exclude = {"genres", "comments"})
@EqualsAndHashCode(exclude = {"genres", "comments"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "script")
public class Script {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @BatchSize(size = 1000)
    @ManyToMany
    @JoinTable(name = "script_genre",
            joinColumns = @JoinColumn(name = "script_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id")
    )
    private Set<Genre> genres;

    @OneToMany(mappedBy = "script")
    private Set<Comment> comments;

    @Column(name = "mongo_id")
    private String mongoId;
}
