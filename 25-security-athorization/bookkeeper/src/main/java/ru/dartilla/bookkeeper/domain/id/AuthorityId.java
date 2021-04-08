package ru.dartilla.bookkeeper.domain.id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AuthorityId implements Serializable {

    @Id
    private String login;

    @Id
    private String authority;
}
