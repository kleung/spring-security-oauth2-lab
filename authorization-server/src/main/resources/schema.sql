--users
create table if not exists users
(
    id       identity                not null auto_increment primary key,
    username varchar_ignorecase(50)  not null,
    password varchar_ignorecase(200) not null,
    enabled  boolean                 not null
);

create unique index if not exists ix_users_username on users (username);

create table if not exists authorities
(
    id        identity               not null auto_increment primary key,
    user_id   bigint                 not null,
    authority varchar_ignorecase(50) not null,
    constraint fk_authorities_users foreign key (user_id) references users (id)
);

create unique index if not exists ix_auth_userid on authorities (user_id, authority);

create sequence if not exists user_seq start with 1 maxvalue 9223372036854775807 no cache nocycle increment by 1;

create sequence if not exists authority_seq start with 1 maxvalue 9223372036854775807 no cache nocycle increment by 1;

--oauth2 clients
create table if not exists oauth_client_details
(
    client_id               varchar(256) not null primary key,
    resource_ids            varchar(256),
    client_secret           varchar_ignorecase(256),
    scope                   varchar(256),
    authorized_grant_types  varchar(256),
    web_server_redirect_uri varchar(256),
    authorities             varchar(256),
    access_token_validity   int,
    refresh_token_validity  int,
    additional_information  varchar(4096),
    autoapprove             varchar(256)
);
