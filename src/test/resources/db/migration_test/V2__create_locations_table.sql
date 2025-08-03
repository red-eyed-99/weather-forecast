CREATE TABLE locations
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(100) NOT NULL UNIQUE,
    latitude  DECIMAL(6,4) NOT NULL,
    longitude DECIMAL(7,4) NOT NULL,

    CONSTRAINT name_check CHECK (
        name REGEXP '^(?=.*[A-Za-z])[A-Za-z\\d\\s\-,.’''&()/]{1,100}$'
    ),
    CONSTRAINT latitude_check CHECK(latitude >= -90 AND latitude <= 90),
    CONSTRAINT longitude_check CHECK(longitude >= -180 AND longitude <= 180)
);