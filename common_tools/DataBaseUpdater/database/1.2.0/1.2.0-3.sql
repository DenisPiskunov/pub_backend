DROP TABLE service_type;

CREATE TABLE service_type
(
    id            SERIAL,
    name          VARCHAR(50)               NOT NULL,
    is_deleted    BOOLEAN     DEFAULT FALSE,
    creation_date TIMESTAMPTZ DEFAULT NOW() NOT NULL,

    CONSTRAINT pk_service_type PRIMARY KEY (id),
    CONSTRAINT uq_service_type_name UNIQUE (name)
);

INSERT INTO version (version)
VALUES ('1.2.0-3');