package ru.dartilla.examinator.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    public String name;
    public String surname;
}
