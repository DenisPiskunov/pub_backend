CREATE TABLE app_settings
(
  id   SERIAL,
  setting_name  VARCHAR(100) NOT NULL,
  setting_value VARCHAR(200),

  CONSTRAINT pk_app_settings_id PRIMARY KEY (id),
  CONSTRAINT uk_setting_name UNIQUE (setting_name)
);

INSERT INTO app_settings(setting_name, setting_value) VALUES ('SENDER_EMAIL', 'noreplay@mint-mobile.ru');
INSERT INTO app_settings(setting_name, setting_value) VALUES ('SENDER_PASSWORD', 'n0rep1ay');
INSERT INTO app_settings(setting_name, setting_value) VALUES ('MAIL_SMTP_AUTH', 'true');
INSERT INTO app_settings(setting_name, setting_value) VALUES ('MAIL_SMTP_STARTTLS_ENABLE', 'true');
INSERT INTO app_settings(setting_name, setting_value) VALUES ('MAIL_SMTP_HOST', 'smtp.gmail.com');
INSERT INTO app_settings(setting_name, setting_value) VALUES ('MAIL_SMTP_PORT', '587');

INSERT INTO version (version) VALUES ('1.5');