package ru.dartilla.bookkeeper.mms.mongo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Жанр
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "genre")
public class Genre {

    @Id
    private String id;

    private String name;
}
