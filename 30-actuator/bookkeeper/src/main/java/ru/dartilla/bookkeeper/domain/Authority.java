package ru.dartilla.bookkeeper.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dartilla.bookkeeper.domain.id.AuthorityId;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(AuthorityId.class)
@Table(name = "authority")
public class Authority {

    @Id
    @Column(name = "login")
    private String login;

    @Id
    @Column(name = "authority")
    private String authority;
}
