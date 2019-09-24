# 初始化 ad 数据库
create database if not exists ad;

# 初始化表
drop table if exists ad_generic_user ;
create table  ad_generic_user
(
    username     varchar(255) not null
        primary key,
    avatar       varchar(255) null,
    birth_day    date         null,
    email        varchar(255) null,
    id_card      varchar(255) null,
    mobile_phone varchar(255) null,
    nick_name    varchar(255) null,
    password     varchar(255) null,
    real_name    varchar(255) null,
    sex         varchar(20)  null,
    user_role    int          null
)
    engine = MyISAM;

INSERT INTO ad.ad_generic_user (username,avatar, birth_day, email, id_card, mobile_phone, nick_name, password, real_name, sex,
                                user_role)
VALUES ('wezhyn', null, '2019-09-20', 'wezhyn@wezhyn.com', '110', '11011011011', '兆他爹',
        '$2a$10$XYnjyrjIGWJ8aczFOcvtOOd13t5gX6DuD02llEv13SX2JnK2KPpWW', null, 'MALE', 1);
INSERT INTO ad.ad_generic_user (username,avatar, birth_day, email, id_card, mobile_phone, nick_name, password, real_name, sex,
                                user_role)
VALUES ('zhaoo', null, '2019-09-05', 'zhaoo@zhaoo.com', '120', '12012012012', '兆儿子',
        '$2a$10$VCpx99qNT3/N52ztT1Zwnelc6UuAJGNFC9cffW56SXteSVY6O6Vii', null, 'MALE', 2);

drop table if exists jwt_user_secret;
create  table jwt_user_secret
(
    username varchar(255) not null
        primary key,
    secret   varchar(255) null
)
    engine = MyISAM;

INSERT INTO ad.jwt_user_secret (username, secret)
VALUES ('wezhyn', 'bCUK2pYEUyc33fmf6PO1V7A2X1L610I0j7ffm5EtO34EmwYtMj');
INSERT INTO ad.jwt_user_secret (username, secret)
VALUES ('zhaoo', 'NdifoHwxa3aHLuixMv1bgybYuZqNgXnHG2MLOAmAGFBVntffub');

