drop database ad_test;
create database ad_test;

use ad_test;
ALTER DATABASE ad_test CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin;

use ad_test;
create table ad_generic_user
(
    id                      int auto_increment
        primary key,
    account_non_expired     bit          default b'1'              null,
    account_non_locked      bit          default b'1'              null,
    avatar                  varchar(255) default ''                null,
    birth_day               datetime                               null,
    email                   varchar(255) default ''                null,
    enable                  varchar(255) default 'NORMAL'          null,
    id_card                 varchar(255) default ''                null,
    intro                   varchar(255) default ''                null,
    last_modified           datetime     default CURRENT_TIMESTAMP null,
    login_time              datetime                               null,
    mobile_phone            varchar(255) default ''                null,
    nick_name               varchar(255) default ''                null,
    password                varchar(255)                           not null,
    real_name               varchar(255) default ''                null,
    reg_time                datetime     default CURRENT_TIMESTAMP null,
    roles                   varchar(255) default 'USER'            null,
    sex                     varchar(255) default 'UNKNOWN'         null,
    username                varchar(255) default ''                null,
    wechat                  varchar(255) default ''                null,
    certification           bit          default b'1'              null,
    credentials_non_expired bit          default b'1'              null,
    constraint UK_rp9kp6ogrkq0e5igtky4ip1yv
        unique (username)
);

INSERT INTO ad_generic_user (id, account_non_expired, account_non_locked, avatar, birth_day, email, enable, id_card,
                             intro, last_modified, login_time, mobile_phone, nick_name, password, real_name, reg_time,
                             roles, sex, username, wechat, certification, credentials_non_expired)
VALUES (1, false, false, '2019-12-26T14:16:08.912&zhaoo&47wFtq1CydIPODK0cjXmxoMean2gh6lS.jpg', '1999-02-04 00:00:00',
        '', 'NOT_AUTHENTICATION', '330624199911111111', '', '2019-12-24 19:35:36', null, '13456789999', 'zhaoo',
        '$2a$10$r6eoy63fWzpf7rNaNYxQtuw0YDVfxyAjsUzZJrxfTQ2xspwXAmDeC', '兆兆', '2019-12-24 19:35:36', 'USER', 'UNKNOWN',
        'zhaoo', '', true, true);
INSERT INTO ad_generic_user (id, account_non_expired, account_non_locked, avatar, birth_day, email, enable, id_card,
                             intro, last_modified, login_time, mobile_phone, nick_name, password, real_name, reg_time,
                             roles, sex, username, wechat, certification, credentials_non_expired)
VALUES (2, false, false, '2019-12-26T14:16:08.912&zhaoo&47wFtq1CydIPODK0cjXmxoMean2gh6lS.jpg', '2019-12-26 00:00:00',
        '', 'NOT_AUTHENTICATION', '', '', '2019-12-26 22:00:31', null, '111', '',
        '$2a$10$DO6v7Zz2p4uyVPcSou1YKuu0Bl4XmAesakaFeBvXzY7i0drlQoTbu', '', '2019-12-26 22:00:31', 'USER', 'UNKNOWN',
        'van', '', true, true);
INSERT INTO ad_generic_user (id, account_non_expired, account_non_locked, avatar, birth_day, email, enable, id_card,
                             intro, last_modified, login_time, mobile_phone, nick_name, password, real_name, reg_time,
                             roles, sex, username, wechat, certification, credentials_non_expired)
VALUES (5, false, false, '', null, '', 'NORMAL', '', '', '2020-01-01 19:30:24', null, '123', '',
        '$2a$10$0NHfDOGKysAaHzSnjyyom.WQ2RLgKXsjPcfIMQw8LWYcGAwCnIyJ6', '', '2020-01-01 19:30:24', 'USER', 'UNKNOWN',
        'aaa', '', false, false);
create table ad_admin
(
    id        int auto_increment
        primary key,
    auth_time datetime     default CURRENT_TIMESTAMP null,
    avatar    varchar(255)                           null,
    email     varchar(255) default ''                null,
    id_card   varchar(255)                           null,
    nick_name varchar(255)                           null,
    password  varchar(255)                           null,
    roles     varchar(255) default 'ADMIN'           null,
    sex       varchar(255) default 'UNKNOWN'         null,
    username  varchar(255)                           null,
    constraint UK_q67efdnclo8mc2hoyapmw9xk3
        unique (username)
);

INSERT INTO ad_admin (id, auth_time, avatar, email, id_card, nick_name, password, roles, sex, username)
VALUES (1, '2019-12-24 18:21:35', '2019-12-26T14:16:08.912&zhaoo&47wFtq1CydIPODK0cjXmxoMean2gh6lS.jpg', '', null,
        'zhaoo', '$2a$10$UTpU5ZXjczxzsoAmHQhGnOrCTAg9DH8s2XDR808hdA3O2UG4NH9Je', 'DEVELOPER', 'UNKNOWN', 'zhaoo');
INSERT INTO ad_admin (id, auth_time, avatar, email, id_card, nick_name, password, roles, sex, username)
VALUES (2, '2019-12-26 14:15:03', '2019-12-26T14:15:00.459&zhaoo&PE0Vszu2IeLBMAK4DNcQfokq867jp9gX.jpg', '', null, '温儿子',
        null, 'DEVELOPER', 'UNKNOWN', 'wezhyn');
create table ad_activity
(
    id           int auto_increment
        primary key,
    a_id         int                                null,
    content      varchar(255)                       null,
    publish      bit      default b'1'              null,
    publish_time datetime default CURRENT_TIMESTAMP null,
    title        varchar(255)                       null,
    constraint FKstqp2mcasw8ipyq80y9irp42s
        foreign key (a_id) references ad_admin (id)
);
INSERT INTO ad_activity (id, a_id, content, publish, publish_time, title)
VALUES (2, 1, '<p>大家一起来抢钱啊</p>', true, '2019-12-31 19:52:00', '抢钱活动');
INSERT INTO ad_activity (id, a_id, content, publish, publish_time, title)
VALUES (3, 1, '<p>跨年了，你却还在写代码，年轻人。</p>
<p>2020新年快乐哦。</p>
<p>一个<code>幽灵</code>，共产主义的<code>幽灵</code>，在欧洲游荡。为了对这个<code>幽灵</code>进行神圣的围剿，旧欧洲的一切势力，教皇和沙皇、梅特涅和基佐、法国的激进派和德国的警察，都联合起来了。</p>',
        true, '2019-12-31 19:54:44', '跨年啦');
INSERT INTO ad_activity (id, a_id, content, publish, publish_time, title)
VALUES (4, 1, '<p>明天你就挂科了。</p>', false, '2019-12-31 19:55:10', '挂科活动');
INSERT INTO ad_activity (id, a_id, content, publish, publish_time, title)
VALUES (5, 1, '<p>这是一个测试</p>', false, '2020-01-15 12:38:02', '测试活动');


create table ad_order
(
    id         int auto_increment
        primary key,
    end_date   datetime      null,
    end_time   datetime      null,
    latitude   double        not null,
    longitude  double        not null,
    num        int           null,
    price      double        null,
    rate       int           not null,
    scope      int           not null,
    start_date datetime      null,
    start_time datetime      null,
    uid        int           not null,
    verify     int default 0 null,
    constraint FK4m9etrxyyg74xwnoof8gm4ja2
        foreign key (uid) references ad_generic_user (id)
);
INSERT INTO ad_order (id, end_date, end_time, latitude, longitude, num, price, rate, scope, start_date, start_time, uid,
                      verify)
VALUES (60, '2020-01-02 02:30:06', '2020-01-02 16:00:00', 30.2268, 120.0445, 100, 1, 5, 5, '2020-01-02 02:30:06',
        '2020-01-01 16:00:00', 1, null);
INSERT INTO ad_order (id, end_date, end_time, latitude, longitude, num, price, rate, scope, start_date, start_time, uid,
                      verify)
VALUES (61, '2020-01-02 03:14:25', '2020-01-02 16:00:00', 30.227093, 120.044288, 100, 1, 4, 8, '2020-01-02 03:13:55',
        '2020-01-01 16:00:26', 1, null);


create table ad_bill_info
(
    id              int auto_increment
        primary key,
    alipay_trade_no varchar(255) null,
    buyer_id        varchar(255) null,
    gmt_create      datetime     null,
    gmt_payment     datetime     null,
    order_id        int          null,
    out_biz_no      varchar(255) null,
    pay_type        int          null,
    seller_id       varchar(255) null,
    total_amount    double       null,
    trade_status    varchar(255) null,
    constraint FKbff0rsreyoohtmynxvqflpb12
        foreign key (order_id) references ad_order (id)
);

INSERT INTO ad_bill_info (id, alipay_trade_no, buyer_id, gmt_create, gmt_payment, order_id, out_biz_no, pay_type,
                          seller_id, total_amount, trade_status)
VALUES (13, null, null, null, null, 60, null, null, null, 100, 'WAIT_BUYER_PAY');
INSERT INTO ad_bill_info (id, alipay_trade_no, buyer_id, gmt_create, gmt_payment, order_id, out_biz_no, pay_type,
                          seller_id, total_amount, trade_status)
VALUES (14, null, null, null, null, 61, null, null, null, 100, 'WAIT_BUYER_PAY');
create table ad_equipment
(
    id          int auto_increment
        primary key,
    create_time datetime     default CURRENT_TIMESTAMP null,
    end_time    datetime                               null,
    feedback    varchar(255) default ''                null,
    img         varchar(255)                           null,
    intro       varchar(255) default '暂无介绍'            null,
    `key`       varchar(255)                           null,
    latitude    double       default 0                 null,
    longitude   double       default 0                 null,
    name        varchar(255) default '暂无'              null,
    start_time  datetime     default CURRENT_TIMESTAMP null,
    status      bit          default b'1'              null,
    uid         int                                    null,
    verify      int          default 0                 null,
    constraint FK9p1iyqm5ky7f6bfwhcbhrjlbp
        foreign key (uid) references ad_generic_user (id)
);



INSERT INTO ad_equipment (id, create_time, end_time, feedback, img, intro, `key`, latitude, longitude, name, start_time,
                          status, uid, verify)
VALUES (2, '2019-12-26 13:28:42', null, '冲不动了', null, '特斯拉', '20191226', 0, 0, '特斯拉', '2019-12-26 13:28:42', false, 1,
        1);
INSERT INTO ad_equipment (id, create_time, end_time, feedback, img, intro, `key`, latitude, longitude, name, start_time,
                          status, uid, verify)
VALUES (3, '2019-12-26 14:06:12', null, '龙鸣车', null, '卡宴', '201912262132', 0, 0, '卡宴', '2019-12-26 14:06:12', false, 1,
        0);
INSERT INTO ad_equipment (id, create_time, end_time, feedback, img, intro, `key`, latitude, longitude, name, start_time,
                          status, uid, verify)
VALUES (4, '2019-12-26 14:06:46', null, '跑车不可以的哦', null, '保时捷911', '20191226123213', 0, 0, '保时捷911',
        '2019-12-26 14:06:46', false, 1, -1);
INSERT INTO ad_equipment (id, create_time, end_time, feedback, img, intro, `key`, latitude, longitude, name, start_time,
                          status, uid, verify)
VALUES (5, '2019-12-26 22:05:48', null, '', null, 'faq', 'xd—001', 0, 0, '摩托罗拉', '2019-12-26 22:05:48', false, 2, 0);
INSERT INTO ad_equipment (id, create_time, end_time, feedback, img, intro, `key`, latitude, longitude, name, start_time,
                          status, uid, verify)
VALUES (6, '2020-01-01 19:31:22', null, '', null, '突突突', 'TL－1001', 0, 0, '拖拉机', '2020-01-01 19:31:22', false, 5, 0);

create table ad_img_bed
(
    id       int auto_increment
        primary key,
    address  varchar(255) null,
    filename varchar(255) null,
    t_index  int          null,
    type     varchar(255) null
);
INSERT INTO ad_img_bed (id, address, filename, t_index, type)
VALUES (11, '2019-12-26T13:19:16.879&zhaoo&ChMlWV0cPsyIcz8EAA40CLystkcAALfyQJZzA0ADjQg394.jpg',
        'ChMlWV0cPsyIcz8EAA40CLystkcAALfyQJZzA0ADjQg394.jpg', 0, 'GUIDE');
INSERT INTO ad_img_bed (id, address, filename, t_index, type)
VALUES (12, '2019-12-26T13:19:33.768&zhaoo&1a6977dfcc8866ec70d024383332342f.jpeg',
        '1a6977dfcc8866ec70d024383332342f.jpeg', 1, 'GUIDE');
INSERT INTO ad_img_bed (id, address, filename, t_index, type)
VALUES (13, '2019-12-26T13:19:40.251&zhaoo&a4fe2b887d7802f7b54b626381600977.jpg',
        'a4fe2b887d7802f7b54b626381600977.jpg', 2, 'GUIDE');
INSERT INTO ad_img_bed (id, address, filename, t_index, type)
VALUES (14, '2019-12-26T13:20:12.028&zhaoo&6b3377f9400c5764bcb8246fc6167408.png',
        '6b3377f9400c5764bcb8246fc6167408.png', null, 'SHUFFING');
INSERT INTO ad_img_bed (id, address, filename, t_index, type)
VALUES (15, '2019-12-26T13:20:40.227&zhaoo&fad31f62bd15445cf5c2dba5ab6da333.png',
        'fad31f62bd15445cf5c2dba5ab6da333.png', null, 'SHUFFING');
INSERT INTO ad_img_bed (id, address, filename, t_index, type)
VALUES (16, '2019-12-26T13:20:55.073&zhaoo&271cd69dbc212f010532c4ad33e214e0.jpeg',
        '271cd69dbc212f010532c4ad33e214e0.jpeg', null, 'SHUFFING');
INSERT INTO ad_img_bed (id, address, filename, t_index, type)
VALUES (17, '2019-12-26T13:21:04.167&zhaoo&922646a14560d76c8af453aca21fabcd.jpeg',
        '922646a14560d76c8af453aca21fabcd.jpeg', null, 'SHUFFING');
INSERT INTO ad_img_bed (id, address, filename, t_index, type)
VALUES (18, '2019-12-26T13:21:15.680&zhaoo&93081e633ea8a156dddc1d6be0194b57.jpeg',
        '93081e633ea8a156dddc1d6be0194b57.jpeg', null, 'SHUFFING');
INSERT INTO ad_img_bed (id, address, filename, t_index, type)
VALUES (19, '2019-12-26T13:21:44.303&zhaoo&94d0fc48c4f1a0078208e99c9e009aa7.png',
        '94d0fc48c4f1a0078208e99c9e009aa7.png', null, 'SHUFFING');
create table ad_msg
(
    mid     int          not null
        primary key,
    content varchar(255) not null,
    time    datetime     not null
);


create table ad_value
(
    id  int auto_increment
        primary key,
    val varchar(255) null
);


INSERT INTO ad_value (id, val)
VALUES (2, '温儿子贱买');
INSERT INTO ad_value (id, val)
VALUES (3, '一块钱四个');
INSERT INTO ad_value (id, val)
VALUES (4, '嘿嘿');
INSERT INTO ad_value (id, val)
VALUES (5, '垃圾温儿子');
INSERT INTO ad_value (id, val)
VALUES (6, '你要挂课了');
INSERT INTO ad_value (id, val)
VALUES (7, '楼上都是傻逼');
INSERT INTO ad_value (id, val)
VALUES (8, '111');
INSERT INTO ad_value (id, val)
VALUES (9, '222');
INSERT INTO ad_value (id, val)
VALUES (10, '温儿子');

create table ad_order_value_list
(
    order_id      int not null,
    value_list_id int not null,
    constraint UK_m8wfc14hefck5g3xmx3422xbj
        unique (value_list_id),
    constraint FK609jcbeuybygbocw4wmv0207s
        foreign key (value_list_id) references ad_value (id),
    constraint FK6a67938si0hld64bba4p4tx1e
        foreign key (order_id) references ad_order (id)
);

INSERT INTO ad_order_value_list (order_id, value_list_id)
VALUES (60, 10);
create table jwt_user_secret
(
    id       int          not null
        primary key,
    secret   varchar(255) null,
    username varchar(255) null
);

INSERT INTO jwt_user_secret (id, secret, username)
VALUES (1, 'zd24zHw44ddFAS2h7b5YFv7nkQ4cFXDpwi89WAHtOoAEEup00T', 'zhaoo');
INSERT INTO jwt_user_secret (id, secret, username)
VALUES (2, 'Sl2cpgYpqiBvK2hpDjQPiOnldnIuF8t98X4VPT1NkvhXGpd7US', 'van');
INSERT INTO jwt_user_secret (id, secret, username)
VALUES (5, 'R7GJ2L0TGkSSl49VHRzvBC8oGhoEBZXBzB3AqkkJWY44iB0VbF', 'aaa');