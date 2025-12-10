CREATE TABLE public.apps_statistic
(
    id SERIAL PRIMARY KEY NOT NULL,
    deleted_date DATE NOT NULL,
    total_apps_count INT NOT NULL,
    active_apps_count INT NOT NULL,
    deleted_apps_count INT NOT NULL
);
CREATE UNIQUE INDEX apps_statistic_id_uindex ON public.apps_statistic (id);

ALTER TABLE public.apps_statistic ADD platform PLATFORM_TYPE NOT NULL;



INSERT INTO app_settings(setting_name, setting_value) VALUES ('TOP_PARSER_ANDROID_MAIN_CONTAINER_KEY', 'div[class=LXrl4c]');
INSERT INTO app_settings(setting_name, setting_value) VALUES ('TOP_PARSER_ANDROID_DESCRIPTION_KEY', 'div[itemprop=description]');
INSERT INTO app_settings(setting_name, setting_value) VALUES ('TOP_PARSER_ANDROID_REVIEWS_KEY', 'AF_initDataCallback({key: ''ds:13'',');
INSERT INTO app_settings(setting_name, setting_value) VALUES ('TOP_PARSER_ANDROID_SCREENSHOTS_KEY', 'img[class=T75of lxGQyd]');
INSERT INTO app_settings(setting_name, setting_value) VALUES ('TOP_PARSER_ANDROID_TITLE_KEY', 'h1[itemprop=name]');
INSERT INTO app_settings(setting_name, setting_value) VALUES ('TOP_PARSER_ANDROID_ICON_URL_KEY', 'img[class=T75of ujDFqe]');



INSERT INTO version (version) VALUES ('1.16');