INSERT INTO mooc_users(id, username, `name`,email, mobile, password_hash, enabled, account_non_expired, account_non_locked, credentials_non_expired, using_mfa) VALUES
(1, 'user', 'Zhang San', 'zhang.san@local.dev', '18621580683', '{BCRYPT}$2a$10$SBJtakWcOw2nPEdLwHuvwOBG6WPGijlX4qQLNDIMrrntjDfhczUz2', true, true, true, true, true),
(2, 'old_user', 'Li Si','li.si@local.dev','13917237348', '{SHA-1}{3jACW3azISsqV8e2oCi17F4jM4wDucD9LB3oI7lDvSU=}a73aee48aa7cd4e2314f2b798854aeb4b1babaf3', true, true, true, true, true);

INSERT INTO mooc_roles(id, role_name) VALUES
(1, 'ROLE_USER'),
(2, 'ROLE_ADMIN');

INSERT INTO mooc_users_roles(user_id, role_id) VALUES
(1, 1),
(1, 2),
(2, 1);