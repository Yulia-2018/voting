DELETE FROM user_roles;
DELETE FROM dishes;
DELETE FROM votes;
DELETE FROM restaurants;
DELETE FROM users;
ALTER SEQUENCE global_seq
RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', '{noop}password'),
  ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001),
  ('ROLE_USER', 100001);

INSERT INTO restaurants (name) VALUES
  ('Элис'), -- 100002
  ('Местечко'); -- 100003

INSERT INTO dishes (name, price, date, restaurant_id) VALUES
  ('Картошечка', 100, '2019-01-01', 100002), -- 100004
  ('Шашлычок', 250, '2019-01-01', 100002),
  ('Компот', 50, '2019-02-01', 100002),
  ('Цезарь', 230, '2019-02-01', 100002),
  ('Суп харчо', 200, '2019-01-01', 100003),
  ('Тирамису', 180, '2019-01-01', 100003),
  ('Мороженое', 150, '2019-02-01', 100003),
  ('Коктейль', 280, '2019-02-01', 100003); -- 100011

INSERT INTO votes (date, user_id, restaurant_id) VALUES
  ('2019-01-01', 100000, 100002),
  ('2019-01-01', 100001, 100002),
  ('2019-02-01', 100000, 100002),
  ('2019-02-01', 100001, 100003),
  (sysdate, 100000, 100003);