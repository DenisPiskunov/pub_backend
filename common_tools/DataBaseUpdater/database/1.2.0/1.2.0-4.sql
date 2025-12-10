CREATE TYPE ACTION_TYPE AS ENUM ('CREATE', 'UPDATE', 'DELETE');

CREATE TABLE logger
(
    id           SERIAL,
    account_uuid UUID                      NOT NULL,
    db_object    VARCHAR                   NOT NULL,
    action_type  ACTION_TYPE               NOT NULL,
    old_val      TEXT,
    new_val      TEXT,
    action_date  TIMESTAMPTZ DEFAULT NOW() NOT NULL,

    CONSTRAINT pk_logger PRIMARY KEY (id)
);


INSERT INTO version (version)
VALUES ('1.2.0-4');