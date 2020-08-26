--liquibase formatted sql

--changeset dartilla:2020-07-18--02-base-tables-data
insert into author (id, name)
values (1, 'Кастанеда К.'),
       (2, 'Дойль A. К.');

insert into genre (id, name)
values (1, 'Мистика'),
       (2, 'Детектив'),
       (3, 'Эзотерика');

insert into script (id, title, author_id)
values (1, 'Учение дона Хуана', 1),
       (2, 'Отдельная реальность', 1),
       (3, 'Отдельная реальность', 1),
       (4, 'Отдельная реальность', 1);

insert into book (id, script_id, in_storage)
values (1, 1, 1),
       (2, 1, 0),
       (3, 3, 1),
       (4, 4, 1);

insert into script_genre (script_id, genre_id)
values (1, 1),
       (2, 1),
       (3, 1),
       (4, 1),
       (4, 3);

insert into comment (id, script_id, parent_id, message)
values (1, 1, null, 'Что курил автор?'),
       (2, 1, 1, 'Читай дальше, там написано'),
       (3, 1, null, 'Какая гадость эта ваша заливная рыба');
