CREATE TABLE users
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL,

    CONSTRAINT username_check CHECK (username REGEXP '^[a-zA-Z\\d_]{5,20}$'),
    CONSTRAINT password_encryption_check CHECK (password REGEXP '^\\$2[aby]\\$\\d{2}\\$[.\/A-Za-z0-9]{53}$')
);