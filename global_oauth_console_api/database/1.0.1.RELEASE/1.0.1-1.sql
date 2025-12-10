CREATE TABLE account
(
    uuid          UUID                        NOT NULL,
    creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_account PRIMARY KEY (uuid)
);

INSERT INTO account (uuid)
VALUES ('b2ed3290-388e-4584-a64f-209c030a648b');

CREATE TABLE account_to_role
(
    account_uuid UUID   NOT NULL,
    role_id      BIGINT NOT NULL,

    CONSTRAINT pk_account_to_role PRIMARY KEY (account_uuid, role_id),
    CONSTRAINT fk_account_to_role_account_uuid FOREIGN KEY (account_uuid) REFERENCES account (uuid),
    CONSTRAINT fk_account_to_role_role_id FOREIGN KEY (role_id) REFERENCES role (id)
);

INSERT INTO account_to_role (account_uuid, role_id)
SELECT acc.uuid, r.id
FROM account AS acc,
     role AS r;

INSERT INTO version (version)
VALUES ('1.0.1-1');