INSERT INTO mooc_users(username, password, enabled) VALUES
('user', '{BCRYPT}$2a$10$SBJtakWcOw2nPEdLwHuvwOBG6WPGijlX4qQLNDIMrrntjDfhczUz2', 1),
('old_user','{SHA-1}{3jACW3azISsqV8e2oCi17F4jM4wDucD9LB3oI7lDvSU=}a73aee48aa7cd4e2314f2b798854aeb4b1babaf3', 1);

INSERT INTO mooc_authorities(username, authority) VALUES
('old_user', 'ROLE_USER'),
('user', 'ROLE_USER'),
('user', 'ROLE_ADMIN');