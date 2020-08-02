--liquibase formatted sql

--changeset dartilla:2020-07-18--02-base-tables-data
insert into author (id, name)
values (1, 'Кастанеда К.'),
       (2, 'Дойль A. К.');

insert into genre (id, name)
values (1, 'Мистика'),
       (2, 'Детектив');

insert into book (id, title, author_id, genre_id, in_storage)
values (1, 'Учение дона Хуана', 1, 1, 1),
       (2, 'Отдельная реальность', 1, 1, 0),
       (3, 'Отдельная реальность', 1, 1, 1),
       (4, 'Отдельная реальность', 1, 1, 1);

