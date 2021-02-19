package ru.dartilla.bookkeeper.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * Рукопись (книга как литературное произведение)
 */
@Data
@ToString(exclude = {"genres", "comments"})
@EqualsAndHashCode(exclude = {"genres", "comments"})
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "script")
public class Script {

    @Id
    private String id;

    private String title;

    private Author author;

    private Set<Genre> genres;
}
