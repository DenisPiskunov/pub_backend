CREATE TABLE app_key_gambling
(
  id           SERIAL    NOT NULL
    CONSTRAINT app_key_gambling_pkey
    PRIMARY KEY,
  key          TEXT      NOT NULL,
  created_date TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX app_key_gambling_key
  ON app_key_gambling (key);


CREATE TABLE exclude_words
(
  id           SERIAL    NOT NULL
    CONSTRAINT exclude_words_pkey
    PRIMARY KEY,
  key       TEXT      NOT NULL,
  created_date TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX exclude_words_key
  ON exclude_words (key);


INSERT INTO app_key_gambling (key, created_date) VALUES ('вулкан', current_timestamp);
INSERT INTO app_key_gambling (key, created_date) VALUES ('джой', current_timestamp);
INSERT INTO app_key_gambling (key, created_date) VALUES ('джойказино', current_timestamp);

INSERT INTO exclude_words (key, created_date) VALUES ('en.funkyjewels.million', current_timestamp);
INSERT INTO exclude_words (key, created_date) VALUES ('покер', current_timestamp);
INSERT INTO exclude_words (key, created_date) VALUES ('poker', current_timestamp);

INSERT INTO version (version) VALUES ('1.11');

