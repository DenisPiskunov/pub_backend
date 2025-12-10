CREATE TABLE refresh_token
(
    id              SERIAL,
    token           CHARACTER(60)               NOT NULL,
    account_uuid    UUID               NOT NULL,
    expiration_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT pk_refresh_token PRIMARY KEY (id),
    CONSTRAINT uq_refresh_token_token UNIQUE (token),
    CONSTRAINT fk_refresh_token_account_uuid FOREIGN KEY (account_uuid) REFERENCES account (uuid)

);

INSERT INTO version (version)
VALUES ('1.0.0-3');