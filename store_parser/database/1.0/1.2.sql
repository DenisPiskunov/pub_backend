CREATE TABLE application_parsing_data
(
  id             SERIAL,
  app_id         INTEGER,
  parsing_result TEXT,
  creation_date  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),

  CONSTRAINT pk_application_parsing_data_id PRIMARY KEY (id),
  CONSTRAINT fk_application_parsing_data_app_id_application_id FOREIGN KEY (app_id)
  REFERENCES application (id) ON UPDATE CASCADE ON DELETE CASCADE
);

ALTER TABLE application ALTER COLUMN icon_url TYPE VARCHAR(200);

INSERT INTO version (version) VALUES ('1.2');