--liquibase formatted sql

--changeset dartilla:2021-03-15--02-user-data
insert into user (id, login, password, enabled)
values (1000, 'user1', '$2a$10$ufDu0Ygp5cGOgwFCi0PovuGHopqum.azwJh87g/0Z2jkYMNIIiOH6', 1),
       (1001, 'user2', '$2a$10$XrifvQazPvdKd7sRtLVZV.qSFuwrEm1s4ArBc00gO04EkIMYggDam', 1);

