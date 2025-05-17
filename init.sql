CREATE DATABASE IF NOT EXISTS harmonique_userservice;
CREATE DATABASE IF NOT EXISTS harmonique_songservice;

USE harmonique_userservice;

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

INSERT INTO roles (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_ARTIST');
INSERT INTO roles (id, name) VALUES (3, 'ROLE_ADMIN');

CREATE USER IF NOT EXISTS 'harmonique_user'@'%' IDENTIFIED BY 'harmonique_pass';
GRANT ALL PRIVILEGES ON harmonique_userservice.* TO 'harmonique_user'@'%';
GRANT ALL PRIVILEGES ON harmonique_songservice.* TO 'harmonique_user'@'%';

FLUSH PRIVILEGES;