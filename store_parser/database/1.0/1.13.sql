INSERT INTO app_settings(setting_name, setting_value) VALUES ('PARSING_DATA_UPDATE_INTERVAL', '259200000');
INSERT INTO app_settings(setting_name, setting_value) VALUES ('PARSE_TOP_INTERVAL', '86400000');
INSERT INTO app_settings(setting_name, setting_value) VALUES ('APPS_PINGER_INTERVAL', '3600000');
-- INSERT INTO app_settings(setting_name, setting_value) VALUES ('PARSING_DATA_UPDATE_INTERVAL', '0 0 22 */3 * *');
-- INSERT INTO app_settings(setting_name, setting_value) VALUES ('PARSE_TOP_INTERVAL', '0 0 6 * * *');
-- INSERT INTO app_settings(setting_name, setting_value) VALUES ('APPS_PINGER_INTERVAL', '0 50 * * * *');

ALTER TABLE application ADD deleted_date TIMESTAMP NULL;
ALTER TABLE application ADD keyword VARCHAR(128) NULL;

INSERT INTO version (version) VALUES ('1.13');