--liquibase formatted sql

--changeset dartilla:2020-07-18--02-base-tables-data
insert into author (id, name)
values (1000, 'А. Лисина'),
       (1001, 'Э. М. Ремарк'),
       (1002, 'Народная');

insert into genre (id, name)
values (1000, 'Детская книга'),
       (1001, 'Драма');

insert into book (id, title, author_id, genre_id, in_storage)
values (1000, 'Дикий пёс', 1000, 1000, 1),
       (1002, 'Дикий пёс', 1000, 1000, 0),
       (1003, 'Три товарища', 1001, 1001, 1),
       (1004, 'Колобок', 1002, 1000, 1);

