CREATE TABLE version
(
  version CHARACTER VARYING(30)       NOT NULL,
  date    TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
  CONSTRAINT uk_version_version UNIQUE (version)
);

INSERT INTO version (version) VALUES ('1.0');