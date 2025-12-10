ALTER TYPE PLATFORM_TYPE ADD VALUE 'WINDOWS';

CREATE TABLE users
(
    id SERIAL PRIMARY KEY NOT NULL,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(256) NOT NULL,
    roles VARCHAR(256) NOT NULL
);
CREATE UNIQUE INDEX users_id_uindex ON users (id);
CREATE UNIQUE INDEX users_username_uindex ON users (username);

INSERT INTO version (version) VALUES ('1.9');
