INSERT INTO ad.ad_generic_user (user_id, avatar, birth_day, email, id_card, mobile_phone, nick_name, password, real_name, sex, user_role, username) VALUES (1, null, '2019-09-20', 'wezhyn@wezhyn.com', '110', '11011011011', '兆他爹', '$2a$10$XYnjyrjIGWJ8aczFOcvtOOd13t5gX6DuD02llEv13SX2JnK2KPpWW', null, 0, 1, 'wezhyn');
INSERT INTO ad.ad_generic_user (user_id, avatar, birth_day, email, id_card, mobile_phone, nick_name, password, real_name, sex, user_role, username) VALUES (2, null, '2019-09-05', 'zhaoo@zhaoo.com', '120', '12012012012', '兆儿子', '$2a$10$VCpx99qNT3/N52ztT1Zwnelc6UuAJGNFC9cffW56SXteSVY6O6Vii', null, 0, 2, 'zhaoo');

INSERT INTO ad.jwt_user_secret (username, secret) VALUES ('wezhyn', 'bCUK2pYEUyc33fmf6PO1V7A2X1L610I0j7ffm5EtO34EmwYtMj');
INSERT INTO ad.jwt_user_secret (username, secret) VALUES ('zhaoo', 'NdifoHwxa3aHLuixMv1bgybYuZqNgXnHG2MLOAmAGFBVntffub');
