CREATE TABLE account
(
    uuid          UUID               NOT NULL,
    login         CHARACTER VARYING(20)       NOT NULL,
    email         CHARACTER VARYING(30)       NOT NULL,
    password      CHARACTER(60)               NOT NULL,
    creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_account PRIMARY KEY (uuid),
    CONSTRAINT uk_account_login UNIQUE (login),
    CONSTRAINT uk_account_email UNIQUE (email)
);

INSERT INTO account (uuid, login, email, password)
VALUES ('b2ed3290-388e-4584-a64f-209c030a648b', 'admin', 'admin@email.com',
        '$2a$10$SC3.An25SJU7cKLFqOxT0.0QtvogHBKSzEu6HPT9GuoF.hv/8DUW6');

INSERT INTO version (version)
VALUES ('1.0.0-2');