CREATE TABLE notification_recipient
(
  id             SERIAL,
  email          CHARACTER VARYING(100),
  CONSTRAINT pk_notification_recipient_id PRIMARY KEY (id)
);

INSERT INTO version (version) VALUES ('1.3');