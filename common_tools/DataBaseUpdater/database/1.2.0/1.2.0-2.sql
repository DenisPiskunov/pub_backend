CREATE TABLE service_type
(
    id            SERIAL
        CONSTRAINT service_type_pk
            PRIMARY KEY,
    name          VARCHAR                                NOT NULL,
    creation_date TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    is_deleted    BOOLEAN                  DEFAULT FALSE
);

CREATE UNIQUE INDEX service_type_id_uindex
    ON service_type (id);

CREATE UNIQUE INDEX service_type_name_uindex
    ON service_type (name);



INSERT INTO version (version)
VALUES ('1.2.0-2');