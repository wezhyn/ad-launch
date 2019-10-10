create table ad_admin
(
    id        varchar(255) not null
        primary key,
    avatar    varchar(255) null,
    email     varchar(255) null,
    id_card   varchar(255) null,
    nick_name varchar(255) null,
    password  varchar(255) null,
    roles     varchar(255) null,
    sex       varchar(255) null,
    username  varchar(255) not null,
    auth_time datetime     null
);

INSERT INTO ad.ad_admin (id, avatar, email, id_card, nick_name, password, roles, sex, username, auth_time) VALUES ('zhaoo', null, 'zhaoo@vip.com', null, '兆儿子', '$2a$10$5r5Tl0auONltEnzD8aTDIeh.qLdEEJEuosQzx9e5jq0I1G8JzGHZ2', 'DEVELOPER', null, '', null);
create table ad_easemob_user
(
    easemob_id varchar(255) not null
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
    sex          varchar(20)  null,
    roles        varchar(50)  null
);

INSERT INTO ad.ad_generic_user (username, avatar, birth_day, email, id_card, mobile_phone, nick_name, password, real_name, sex, roles) VALUES ('wezhyn', 'FqQ2LogL3LZNBFhCjsjwXxvAmVVz', '2019-09-20', 'wezhyn@wezhyn.com', '110', '11011011011', '兆他爹', '$2a$10$XYnjyrjIGWJ8aczFOcvtOOd13t5gX6DuD02llEv13SX2JnK2KPpWW', 'wen', 'MALE', 'CUSTOMER');
INSERT INTO ad.ad_generic_user (username, avatar, birth_day, email, id_card, mobile_phone, nick_name, password, real_name, sex, roles) VALUES ('wezhyn123', '123123', null, null, null, null, null, null, null, null, 'CUSTOMER');
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