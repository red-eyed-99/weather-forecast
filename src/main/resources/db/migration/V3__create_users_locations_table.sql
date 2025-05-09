CREATE TABLE users_locations
(
    user_id     BIGINT,
    location_id BIGINT,

    PRIMARY KEY(user_id, location_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (location_id) REFERENCES locations (id) ON DELETE CASCADE
);