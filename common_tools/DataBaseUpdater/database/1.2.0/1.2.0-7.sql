CREATE TABLE payment_system_accounts
(
    id            SERIAL,
    url           VARCHAR(256)              NOT NULL,
    login         VARCHAR(50)               NOT NULL,
    password      VARCHAR(50)               NOT NULL,
    is_deleted    BOOLEAN     DEFAULT FALSE NOT NULL,
    creation_date TIMESTAMPTZ DEFAULT NOW() NOT NULL,

    CONSTRAINT pk_payment_system_accounts PRIMARY KEY (id)
);

ALTER TYPE DB_OBJECTS ADD VALUE 'PAYMENT_SYSTEM_ACCOUNTS';

INSERT INTO version (version)
VALUES ('1.2.0-7');