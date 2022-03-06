drop database if exists restaurant_user;
create database restaurant_user;
use restaurant_user;

create table hibernate_sequence (
    next_val bigint primary key
);
insert into hibernate_sequence values ( 1 );

create table user_data (
                           id bigint not null,
                           email varchar(255) not null,
                           name varchar(255) not null,
                           password varchar(255) not null,
                           primary key (id)
);
create table user_data_roles (
                                 user_data_id bigint not null primary key,
                                 roles_id bigint not null
);
create table user_role (
                           id bigint not null,
                           role_name varchar(255) not null,
                           primary key (id)
);

insert into user_role (id, role_name)
                values (1, 'ROLE_USER');

alter table user_data_roles
    add constraint fk_roles_id
        foreign key (roles_id)
            references user_role (id);

alter table user_data_roles
    add constraint fk_user_data_id
        foreign key (user_data_id)
            references user_data (id);