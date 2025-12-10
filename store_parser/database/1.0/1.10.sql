UPDATE app_settings SET setting_value = 'https://itunes.apple.com/%s/app/id%s' WHERE setting_name = 'ITUNES_APP_URL_TEMPLATE';
INSERT INTO app_settings(setting_name, setting_value) VALUES ('ANDROID_APP_URL_TEMPLATE', 'https://play.google.com/store/apps/details?id=%s&hl=%s');
INSERT INTO app_settings(setting_name, setting_value) VALUES ('ANDROID_APP_URL_TEMPLATE_SIMPLE', 'https://play.google.com/store/apps/details?id=%s');
INSERT INTO app_settings(setting_name, setting_value) VALUES ('ITUNES_APP_URL_TEMPLATE_SIMPLE', 'https://itunes.apple.com/ru/app/id%s');
INSERT INTO app_settings(setting_name, setting_value) VALUES ('IOS_TOP_APPS_URL', 'https://itunes.apple.com/ru/rss/topfreeapplications/limit=200/genre=7006/json');
INSERT INTO app_settings(setting_name, setting_value) VALUES ('ANDROID_TOP_APPS_URL', 'https://play.google.com/store/apps/category/GAME_CASINO/collection/topselling_new_free?start=%s&num=120');

INSERT INTO version (version) VALUES ('1.10');