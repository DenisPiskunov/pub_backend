CREATE TABLE service
(
    id                        SERIAL,
    login                     VARCHAR(50) NOT NULL,
    password                  VARCHAR(50) NOT NULL,
    url                       VARCHAR(200) NOT NULL,
    service_type_id           BIGINT NOT NULL,
    expiration_date           TIMESTAMPTZ,
    is_legal_person           BOOLEAN NOT NULL ,
    comment                   TEXT,
    parent_id                 BIGINT,
    is_deleted                BOOLEAN     DEFAULT FALSE NOT NULL,
    creation_date             TIMESTAMPTZ DEFAULT NOW() NOT NULL,

    CONSTRAINT pk_service PRIMARY KEY (id),

    CONSTRAINT fk_service_service_type_id FOREIGN KEY (service_type_id) REFERENCES service_type (id),

    CONSTRAINT fk_service_parent_id FOREIGN KEY (parent_id) REFERENCES service (id)
);

ALTER TYPE DB_OBJECT ADD VALUE 'SERVICE';

INSERT INTO version (version)
VALUES ('1.2.0-12');