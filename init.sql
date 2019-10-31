create table ad_admin
(
    id        int auto_increment
        primary key,
    auth_time datetime     default CURRENT_TIMESTAMP null,
    avatar    varchar(255)                           null,
    email     varchar(255)                           null,
    id_card   varchar(255)                           null,
    nick_name varchar(255)                           null,
    password  varchar(255)                           null,
    roles     varchar(255)                           null,
    sex       varchar(255) default 'UNKNOWN'         null,
    username  varchar(255)                           null,
    constraint ad_admin_username_uindex
        unique (username)
);

INSERT INTO ad.ad_admin (id, auth_time, avatar, email, id_card, nick_name, password, roles, sex, username)
VALUES (1, null, null, 'zhaoo@vip.com', null, '兆儿子', '$2a$10$5r5Tl0auONltEnzD8aTDIeh.qLdEEJEuosQzx9e5jq0I1G8JzGHZ2',
        'DEVELOPER', 'UNKNOWN', 'zhaoo');
INSERT INTO ad.ad_admin (id, auth_time, avatar, email, id_card, nick_name, password, roles, sex, username)
VALUES (4, '2019-10-31 20:46:04', '123', 'test@test.com', '123123', 'hhhh', '123123', 'USER', 'MALE',
        'wezhyn-register3');
create table ad_easemob_user
(
    easemob_id int          not null
        primary key,
    activated  bit          not null,
    created    varchar(255) null,
    modified   varchar(255) null,
    nickname   varchar(255) null,
    password   varchar(255) null,
    uuid       varchar(255) null
);


create table ad_generic_user
(
    id                      int auto_increment
        primary key,
    account_non_expired     bit          default b'1'              not null,
    account_non_locked      bit          default b'1'              not null,
    avatar                  varchar(255)                           null,
    birth_day               date                                   null,
    certification           bit          default b'0'              not null,
    credentials_non_expired bit          default b'1'              not null,
    email                   varchar(255)                           null,
    enable                  varchar(255) default 'NORMAL'          null,
    id_card                 varchar(255)                           null,
    intro                   varchar(255)                           null,
    last_modified           datetime                               null,
    login_time              datetime                               null,
    mobile_phone            varchar(255)                           null,
    nick_name               varchar(255)                           null,
    password                varchar(255)                           null,
    real_name               varchar(255)                           null,
    reg_time                datetime     default CURRENT_TIMESTAMP null,
    roles                   varchar(255) default 'user'            null,
    sex                     varchar(255)                           null,
    username                varchar(255)                           null,
    wechat                  varchar(255)                           null,
    constraint UK_rp9kp6ogrkq0e5igtky4ip1yv
        unique (username)
);

INSERT INTO ad.ad_generic_user (id, account_non_expired, account_non_locked, avatar, birth_day, certification,
                                credentials_non_expired, email, enable, id_card, intro, last_modified, login_time,
                                mobile_phone, nick_name, password, real_name, reg_time, roles, sex, username, wechat)
VALUES (1, true, true, 'FqQ2LogL3LZNBFhCjsjwXxvAmVVz', '2019-09-20', false, true, 'wezhyn@wezhyn.com', 'NORMAL', '110',
        null, null, null, '11011011011', '兆他爹', '$2a$10$XYnjyrjIGWJ8aczFOcvtOOd13t5gX6DuD02llEv13SX2JnK2KPpWW', 'wen',
        null, 'CUSTOMER', 'MALE', 'wezhyn', null);
INSERT INTO ad.ad_generic_user (id, account_non_expired, account_non_locked, avatar, birth_day, certification,
                                credentials_non_expired, email, enable, id_card, intro, last_modified, login_time,
                                mobile_phone, nick_name, password, real_name, reg_time, roles, sex, username, wechat)
VALUES (2, true, true, '123123', null, false, true, null, 'NORMAL', null, null, null, null, null, null, null, null,
        null, 'CUSTOMER', 'UNKNOWN', 'wezhyn123', null);
INSERT INTO ad.ad_generic_user (id, account_non_expired, account_non_locked, avatar, birth_day, certification,
                                credentials_non_expired, email, enable, id_card, intro, last_modified, login_time,
                                mobile_phone, nick_name, password, real_name, reg_time, roles, sex, username, wechat)
VALUES (3, true, true, 'FqQ2LogL3LZNBFhCjsjwXxvAmVVz', '2019-09-05', false, true, 'zhaoo@zhaoo.com', 'NORMAL', '120',
        null, null, null, '12012012012', '兆儿子', '$2a$10$VCpx99qNT3/N52ztT1Zwnelc6UuAJGNFC9cffW56SXteSVY6O6Vii', null,
        null, 'CUSTOMER', 'MALE', 'zhaoo', null);
create table ad_img_bed
(
    id       int auto_increment
        primary key,
    address  varchar(255) null,
    filename varchar(255) null,
    t_index  int          null,
    type     varchar(255) null
);

INSERT INTO ad.ad_img_bed (id, address, filename, t_index, type) VALUES (1, 'Fmro-VuNwXXYyVS83LuZbs1HDXDl', '1.jpg', 1, 'GUIDE');
create table jwt_user_secret
(
    username varchar(255) not null
        primary key,
    secret   varchar(255) null
);

INSERT INTO ad.jwt_user_secret (username, secret)
VALUES ('zhaoo', 'CdghxXFucFJoDoBbNF22If2J6hWkrtV0nVPipZmmFzVbNt0o3P');
