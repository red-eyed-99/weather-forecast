CREATE TABLE sessions
(
    id         CHAR(36) DEFAULT (uuid()) PRIMARY KEY,
    user_id    BIGINT   NOT NULL,
    expires_at DATETIME NOT NULL,

    CONSTRAINT id_uuid_check CHECK (
        id REGEXP '^[a-fA-F\d]{8}-[a-fA-F\d]{4}-[a-fA-F\d]{4}-[a-fA-F\d]{4}-[a-fA-F\d]{12}$'),

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);