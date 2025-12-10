INSERT INTO app_settings(setting_name, setting_value) VALUES ('ANDROID_TOP_APPS_URL_TEMPLATE', 'https://play.google.com/store/apps/category/%s/collection/topselling_new_free?start=%s&num=120');
INSERT INTO app_settings(setting_name, setting_value) VALUES ('IOS_TOP_APPS_URL_TEMPLATE', 'https://itunes.apple.com/ru/rss/topfreeapplications/limit=200/genre=%s/json');
INSERT INTO app_settings(setting_name, setting_value) VALUES ('WORDS_SEARCH_REGEX', '[^a-zа-яё]%s[^a-zа-яё]|^%s[^a-zа-яё]|[^a-zа-яё]%s$|^%s$');
INSERT INTO app_settings(setting_name, setting_value) VALUES ('STORE_CATEGORIES_ANDROID', 'https://data.42matters.com/api/meta/android/apps/app_categories.json');
INSERT INTO app_settings(setting_name, setting_value) VALUES ('STORE_CATEGORIES_IOS', 'https://data.42matters.com/api/meta/ios/apps/top_chart_genres.json');
INSERT INTO app_settings(setting_name, setting_value) VALUES ('CHECK_CATEGORIES_ON_START', 'true');


CREATE TABLE apps_categories
(
    id SERIAL PRIMARY KEY NOT NULL,
    platform VARCHAR(32),
    category_key VARCHAR(64),
    category_name VARCHAR(128)
);
CREATE UNIQUE INDEX apps_categories_id_uindex ON apps_categories (id);

ALTER TABLE application ADD category VARCHAR(128) NULL;
ALTER TABLE application ADD category_key VARCHAR(64) NULL;
ALTER TABLE application ADD valid BOOLEAN DEFAULT TRUE  NULL;

ALTER TABLE apps_categories ADD search BOOLEAN DEFAULT true NOT NULL;

UPDATE application SET valid = TRUE;
UPDATE application SET category = 'Casino', category_key = 'GAME_CASINO' WHERE platform = 'ANDROID';
UPDATE application SET category = 'Casino', category_key = '7006' WHERE platform = 'IOS';

CREATE TABLE invalid_apps_ids
(
    id SERIAL PRIMARY KEY NOT NULL,
    app_id VARCHAR(128) NOT NULL
);
CREATE UNIQUE INDEX invalid_apps_ids_id_uindex ON invalid_apps_ids (id);


INSERT INTO apps_categories (platform, category_key, category_name, search ) VALUES ('android', 'GAME_CASINO', 'Casino', true);
INSERT INTO apps_categories (platform, category_key, category_name, search ) VALUES ('ios', '7006', 'Casino', true);




INSERT INTO version (version) VALUES ('1.19');



select * from get_active_apps()