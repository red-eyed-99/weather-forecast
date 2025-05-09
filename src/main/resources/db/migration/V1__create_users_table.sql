CREATE TABLE users
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    login    VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL,

    CONSTRAINT min_login_length_check CHECK (LENGTH(login) >= 5),
    CONSTRAINT password_encryption_check CHECK (password REGEXP '^\\$2[aby]\\$\\d{2}\\$[.\/A-Za-z0-9]{53}$')
);