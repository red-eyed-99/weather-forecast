CREATE TABLE sessions
(
    uuid       VARCHAR(36) PRIMARY KEY DEFAULT (uuid()),
    user_id    BIGINT NOT NULL,
    expires_at DATETIME NOT NULL,

    CONSTRAINT uuid_check CHECK(
        uuid REGEXP '^[a-fA-F\\d]{8}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{12}$'),

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);