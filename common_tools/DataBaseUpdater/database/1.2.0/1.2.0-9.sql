ALTER TABLE payment_system_account
    RENAME CONSTRAINT pk_payment_system_accounts TO pk_payment_system_account;

ALTER TYPE DB_OBJECT RENAME VALUE 'PAYMENT_SYSTEM_ACCOUNTS' TO 'PAYMENT_SYSTEM_ACCOUNT';

CREATE TABLE server_type
(
    id            SERIAL,
    name          VARCHAR(50)               NOT NULL,
    is_deleted    BOOLEAN     DEFAULT FALSE NOT NULL,
    creation_date TIMESTAMPTZ DEFAULT NOW() NOT NULL,

    CONSTRAINT pk_server_type PRIMARY KEY (id),
    CONSTRAINT uk_server_type_name UNIQUE (name)
);

ALTER TYPE DB_OBJECT ADD VALUE 'SERVER_TYPE';

INSERT INTO version (version)
VALUES ('1.2.0-9');