DROP TABLE IF EXISTS users;

CREATE TABLE mooc_users (
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    enabled TINYINT NOT NULL DEFAULT 1,
    name VARCHAR(50) NULL,
    PRIMARY KEY (username)
);

DROP TABLE IF EXISTS authorities;

CREATE TABLE mooc_authorities (
    username VARCHAR(50) NOT NULL ,
    authority VARCHAR(50) NOT NULL ,
    CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES mooc_users(username)
);

CREATE UNIQUE INDEX  ix_auth_username ON mooc_authorities (username, authority);