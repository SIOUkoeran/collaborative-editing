DROP TABLE IF exists user;

CREATE TABLE user(
    id          int AUTO_INCREMENT,
    username    varchar(100),
    password    varchar(100),
    email       varchar(100),
    primary key (id)
) ;