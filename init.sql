drop table if exists ad_generic_user;
drop table if exists ad_admin;
drop table if exists ad_equipment;
drop table if exists ad_easemob_user;
drop table if exists jwt_user_secret;
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
    constraint UK_q67efdnclo8mc2hoyapmw9xk3
        unique (username)
);

INSERT INTO ad.ad_admin (id, auth_time, avatar, email, id_card, nick_name, password, roles, sex, username)
VALUES (1, '2019-11-04 20:37:34', null, 'zhaoo@vip.com', null, '兆儿子',
        '$2a$10$U.aFqIKVyeqPpmbR5IgKAer.XOMcC4OCj51myf1VaO.5asnYgUr9G', 'DEVELOPER', null, 'zhaoo');
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


create table ad_equipment
(
    id          int auto_increment
        primary key,
    create_time datetime default CURRENT_TIMESTAMP null,
    end_time    datetime                           null,
    img         varchar(255)                       null,
    intro       varchar(255)                       null,
    `key`       varchar(255)                       null,
    latitude    double                             null,
    longitude   double                             null,
    name        varchar(255)                       null,
    position    varchar(255)                       null,
    start_time  datetime                           null,
    status      bit      default b'1'              not null,
    uid         int                                null,
    verify      bit      default b'0'              not null
);

INSERT INTO ad.ad_equipment (id, create_time, end_time, img, intro, `key`, latitude, longitude, name, position,
                             start_time, status, uid, verify)
VALUES (2, '2019-11-04 20:29:47', '2019-11-04 12:29:29', null, '三菱背板', '2019110401', 30.2225, 120.0325, '三菱', null,
        '2019-11-04 12:29:29', true, 1, false);
INSERT INTO ad.ad_equipment (id, create_time, end_time, img, intro, `key`, latitude, longitude, name, position,
                             start_time, status, uid, verify)
VALUES (3, '2019-11-04 20:30:19', '2019-11-04 12:30:02', null, '特斯拉背板', '2019110402', 30.2243, 120.0328, '特斯拉背', null,
        '2019-11-04 12:30:02', true, 1, false);
INSERT INTO ad.ad_equipment (id, create_time, end_time, img, intro, `key`, latitude, longitude, name, position,
                             start_time, status, uid, verify)
VALUES (4, '2019-11-04 20:30:37', '2019-11-04 12:30:21', null, '现代背板', '2019110403', 30.2236, 120.0332, '现代', null,
        '2019-11-04 12:30:21', true, 1, false);
create table ad_generic_user
(
    id                      int auto_increment
        primary key,
    account_non_expired     bit          default b'1'              not null,
    account_non_locked      bit          default b'1'              not null,
    avatar                  varchar(255) default ''                null,
    birth_day               date                                   null,
    certification           bit          default b'0'              not null,
    credentials_non_expired bit          default b'1'              not null,
    email                   varchar(255) default ''                null,
    enable                  varchar(255) default 'NORMAL'          null,
    id_card                 varchar(255) default ''                null,
    intro                   varchar(255) default ''                null,
    last_modified           datetime                               null,
    login_time              datetime                               null,
    mobile_phone            varchar(255) default ''                null,
    nick_name               varchar(255) default ''                null,
    password                varchar(255)                           not null,
    real_name               varchar(255) default ''                null,
    reg_time                datetime     default CURRENT_TIMESTAMP null,
    roles                   varchar(255) default 'USER'            null,
    sex                     varchar(255)                           null,
    username                varchar(255) default ''                null,
    wechat                  varchar(255) default ''                null,
    constraint UK_rp9kp6ogrkq0e5igtky4ip1yv
        unique (username)
);

INSERT INTO ad.ad_generic_user (id, account_non_expired, account_non_locked, avatar, birth_day, certification,
                                credentials_non_expired, email, enable, id_card, intro, last_modified, login_time,
                                mobile_phone, nick_name, password, real_name, reg_time, roles, sex, username, wechat)
VALUES (1, false, false, ' ', null, false, false, ' ', 'NORMAL', ' ', ' ', null, null, '13456789999', ' ',
        '$2a$10$xNybpLrwRfzTSBPSTHg8bOp6JZf80Z7Y.kljV9uwfZ8gMlWh6JW4q', ' ', '2019-11-04 18:27:29', 'USER', 'UNKNOWN',
        'zhaoo', ' ');
INSERT INTO ad.ad_generic_user (id, account_non_expired, account_non_locked, avatar, birth_day, certification,
                                credentials_non_expired, email, enable, id_card, intro, last_modified, login_time,
                                mobile_phone, nick_name, password, real_name, reg_time, roles, sex, username, wechat)
VALUES (2, true, true, 'FqQ2LogL3LZNBFhCjsjwXxvAmVVz', null, false, true, 'zhaoo@zhaoo.com', 'NORMAL', '120', ' ', null,
        null, '12012012012', '兆儿子', '$2a$10$fIBN.EHdsaN3qJyBXzBMWOg3Wpf87RuA7l0jDTxPDR7gCvLHdYsce', ' ',
        '2019-11-04 19:21:14', 'CUSTOMER', 'UNKNOWN', 'wezhyn', ' ');
create table ad_img_bed
(
    id       int auto_increment
        primary key,
    address  varchar(255) null,
    filename varchar(255) null,
    t_index  int          null,
    type     varchar(255) null
);


create table equipment
(
    id          int auto_increment
        primary key,
    create_time datetime default CURRENT_TIMESTAMP null,
    end_time    datetime                           null,
    img         varchar(255)                       null,
    intro       varchar(255)                       null,
    `key`       varchar(255)                       null,
    name        varchar(255)                       null,
    position    varchar(255)                       null,
    start_time  datetime                           null,
    status      bit      default b'1'              not null,
    uid         varchar(255)                       null,
    verify      bit      default b'0'              not null
);


create table jwt_user_secret
(
    id       int          not null
        primary key,
    secret   varchar(255) null,
    username varchar(255) null
);

INSERT INTO ad.jwt_user_secret (id, secret, username)
VALUES (2, 'pwhhl5S6GG0E8jrAdi0vZdQ8QqRfc96PVAJNaynGWPDeTXU0ie', 'wezhyn');