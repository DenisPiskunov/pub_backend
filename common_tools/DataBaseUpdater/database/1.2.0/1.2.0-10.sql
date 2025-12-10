CREATE TABLE project
(
    id            SERIAL,
    name          VARCHAR(50)               NOT NULL,
    is_deleted    BOOLEAN     DEFAULT FALSE NOT NULL,
    creation_date TIMESTAMPTZ DEFAULT NOW() NOT NULL,

    CONSTRAINT pk_project PRIMARY KEY (id),
    CONSTRAINT uk_project_name UNIQUE (name)
);

ALTER TYPE DB_OBJECT ADD VALUE 'PROJECT';

INSERT INTO version (version)
VALUES ('1.2.0-10');