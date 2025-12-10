CREATE TABLE account_reset_password_secret_code
(
    id              SERIAL,
    code            CHARACTER(15)               NOT NULL,
    account_uuid    UUID                        NOT NULL,
    expiration_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT pk_account_reset_password_secret_code PRIMARY KEY (id),
    CONSTRAINT uq_account_reset_password_secret_code_code UNIQUE (code),
    CONSTRAINT fk_account_reset_password_secret_code_account_id FOREIGN KEY (account_uuid) REFERENCES account (uuid)

);

INSERT INTO version (version)
VALUES ('1.2.0-1');