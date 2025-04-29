CREATE DATABASE IF NOT EXISTS harmonique_userservice;
CREATE DATABASE IF NOT EXISTS harmonique_songservice;

CREATE USER IF NOT EXISTS 'harmonique_user'@'%' IDENTIFIED BY 'harmonique_pass';
GRANT ALL PRIVILEGES ON harmonique_userservice.* TO 'harmonique_user'@'%';
GRANT ALL PRIVILEGES ON harmonique_songservice.* TO 'harmonique_user'@'%';

FLUSH PRIVILEGES;