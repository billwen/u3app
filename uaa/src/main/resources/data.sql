TRUNCATE TABLE mooc_roles_permissions;
TRUNCATE TABLE mooc_users_roles;
TRUNCATE TABLE mooc_permissions;
TRUNCATE TABLE mooc_roles;
TRUNCATE TABLE mooc_users;

INSERT INTO mooc_permissions(id, permission_name, display_name)
    values (1, 'USER_READ', '查询用户信息'),
           (2, 'USER_CREATE', '新建用户'),
           (3, 'USER_UPDATE', '编辑用户信息'),
           (4, 'USER_ADMIN', '用户管理');

INSERT INTO mooc_roles(id, role_name, display_name, built_in)
    values (1, 'ROLE_USER', '客户端用户', true),
           (2, 'ROLE_ADMIN', '超级管理员', true),
           (3, 'ROLE_STAFF', '管理后台用户', true);

INSERT INTO mooc_users(id, username, `name`, mobile, password_hash, enabled, account_non_expired, account_non_locked, credentials_non_expired, using_mfa)
    values (1, 'user', 'Zhang San', 'zhang.san@local.dev', '18621580683', '{BCRYPT}$2a$10$SBJtakWcOw2nPEdLwHuvwOBG6WPGijlX4qQLNDIMrrntjDfhczUz2', true, true, true, true, true),
           (2, 'old_user', 'Li Si','li.si@local.dev','13917237348', '{SHA-1}{3jACW3azISsqV8e2oCi17F4jM4wDucD9LB3oI7lDvSU=}a73aee48aa7cd4e2314f2b798854aeb4b1babaf3', true, true, true, true, true);

INSERT INTO mooc_users_roles(user_id, role_id)
    values (1,1),
           (1,2),
           (1,3),
           (2,1);

INSERT INTO mooc_roles_permissions (role_id, permission_id)
    values (1, 1),
           (2, 1),
           (2,2),
           (2, 3),
           (2, 4);