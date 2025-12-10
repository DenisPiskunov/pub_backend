CREATE TABLE authority
(
    id   SERIAL,
    name CHARACTER VARYING(40) NOT NULL,

    CONSTRAINT pk_authority PRIMARY KEY (id),
    CONSTRAINT uq_authority_name UNIQUE (name)
);

CREATE TABLE role
(
    id            SERIAL,
    name          CHARACTER VARYING(50)       NOT NULL,
    is_deleted    BOOLEAN                     NOT NULL DEFAULT FALSE,
    creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_role PRIMARY KEY (id),
    CONSTRAINT uq_role_name UNIQUE (name)
);

CREATE TABLE role_to_authority
(
    role_id      BIGINT NOT NULL,
    authority_id BIGINT NOT NULL,

    CONSTRAINT pk_role_to_authority PRIMARY KEY (role_id, authority_id),
    CONSTRAINT fk_role_to_authority_role_id FOREIGN KEY (role_id) REFERENCES role (id),
    CONSTRAINT fk_role_to_authority_authority_id FOREIGN KEY (authority_id) REFERENCES authority (id)
);

INSERT INTO authority (name)
VALUES ('ACCOUNT_READ');
INSERT INTO authority (name)
VALUES ('ACCOUNT_CREATE');
INSERT INTO authority (name)
VALUES ('ACCOUNT_UPDATE');

INSERT INTO authority (name)
VALUES ('ROLE_READ');
INSERT INTO authority (name)
VALUES ('ROLE_CREATE');
INSERT INTO authority (name)
VALUES ('ROLE_UPDATE');
INSERT INTO authority (name)
VALUES ('ROLE_DELETE');

INSERT INTO role (name)
VALUES ('ADMIN');

INSERT INTO role_to_authority (role_id, authority_id)
SELECT r.id, auth.id
FROM role AS r,
     authority AS auth;

INSERT INTO version (version)
VALUES ('1.0.0-2');