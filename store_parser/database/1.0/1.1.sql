CREATE TYPE PLATFORM_TYPE AS ENUM ('ANDROID', 'IOS');
CREATE TYPE APP_STATUS_TYPE AS ENUM ('AVAILABLE', 'UNAVAILABLE');

CREATE TABLE application
(
  id            SERIAL,
  app_id        CHARACTER VARYING(100),
  platform      PLATFORM_TYPE               NOT NULL,
  status        APP_STATUS_TYPE             NOT NULL,
  creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
  title         CHARACTER VARYING(100),
  url           CHARACTER VARYING(100),
  icon_url      CHARACTER VARYING(100),
  CONSTRAINT pk_application_id PRIMARY KEY (id)
);


INSERT INTO version (version) VALUES ('1.1');