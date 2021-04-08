--liquibase formatted sql

--changeset dartilla:2021-03-15--02-user-data
insert into user (id, login, password, enabled)
values (1000, 'user', '$2a$10$EzK0LIxfSx2oOOUPVRcI5uqw8Bv9nmjMV0f5TVlqWBgjQcobioYNm', 1), --password=user
       (1001, 'manager', '$2a$10$eFRPh1JsgJdmQT3sYHz8d.FipTRLO53xsEbE7tpLBl72G.M1Z6f92', 1); --password=manager

--changeset dartilla:2021-04-03--01-authority-data
insert into authority (login, authority)
values ('user', 'USER'),
       ('manager', 'MANAGER');

--changeset dartilla:2021-04-03--02-user-data
insert into user (id, login, password, enabled)
values (1002, 'guest', '$2a$10$QuFN2JmXC.BxpsJczaCWCuKv.bMfDYeeJ6E.LceOcg2aVe7H9ru9S', 1); --password=guest
