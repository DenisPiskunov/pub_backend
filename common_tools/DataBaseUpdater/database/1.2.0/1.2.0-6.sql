CREATE TABLE payment_system
(
    id            SERIAL,
    name          VARCHAR(50)               NOT NULL,
    is_deleted    BOOLEAN     DEFAULT FALSE NOT NULL,
    creation_date TIMESTAMPTZ DEFAULT NOW() NOT NULL,

    CONSTRAINT pk_payment_system PRIMARY KEY (id),
    CONSTRAINT uk_payment_system_name UNIQUE (name)
);

ALTER TYPE DB_OBJECTS ADD VALUE 'PAYMENT_SYSTEM';

INSERT INTO version (version)
VALUES ('1.2.0-6');