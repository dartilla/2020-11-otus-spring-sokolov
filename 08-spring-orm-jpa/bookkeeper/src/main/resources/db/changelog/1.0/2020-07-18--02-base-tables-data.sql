--liquibase formatted sql

--changeset dartilla:2020-07-18--02-base-tables-data
insert into author (id, name)
values (1000, 'Лисина A.'),
       (1001, 'Ремарк Э. М.'),
       (1002, 'Народная');

insert into genre (id, name)
values (1000, 'Детская книга'),
       (1001, 'Драма');

insert into script (id, title, author_id)
values (1000, 'Дикий пёс', 1000),
       (1003, 'Три товарища', 1001),
       (1004, 'Колобок', 1002);

insert into book (id, script_id, in_storage)
values (1000, 1000, 1),
       (1001, 1000, 0),
       (1002, 1003, 1),
       (1003, 1004, 1);

insert into script_genre (script_id, genre_id)
values (1000, 1000),
       (1002, 1000),
       (1003, 1001),
       (1004, 1000),
       (1004, 1001);

insert into comment (id, script_id, parent_id, message)
values (1000, 1000, null, 'Пёс - огонь'),
       (1002, 1000, 1000, 'Полностью согласен'),
       (1003, 1001, null, 'Уснул на заглавии');
