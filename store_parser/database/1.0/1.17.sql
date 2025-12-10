create function get_active_apps() returns TABLE(apps_count bigint, platform character varying, stat_date date)
LANGUAGE plpgsql
AS $$
DECLARE
  _min_date DATE;
  _max_date DATE;
  _current_date DATE;

BEGIN
  SELECT MIN(a.creation_date) from application a into _min_date;
  SELECT current_date into _max_date;
  _current_date = _min_date;
  WHILE _current_date <= _max_date LOOP
    stat_date = _current_date;
    platform = 'IOS';
    SELECT
      count(a.app_id)
      FROM application a
    WHERE (CAST(a.deleted_date AS DATE) > _current_date OR a.deleted_date IS NULL)
      AND CAST(a.creation_date as DATE) <= _current_date
      AND CAST(a.platform as VARCHAR(16)) = 'IOS'
    INTO apps_count;
    RETURN NEXT;

   platform = 'ANDROID';
    SELECT
      count(a.app_id)
      FROM application a
    WHERE (CAST(a.deleted_date AS DATE) > _current_date OR a.deleted_date IS NULL)
      AND CAST(a.creation_date as DATE) <= _current_date
      AND CAST(a.platform as VARCHAR(16)) = 'ANDROID'
    INTO apps_count;
    RETURN NEXT;


    _current_date = _current_date + 1;
  END LOOP;


END;
$$;

INSERT INTO app_settings(setting_name, setting_value) VALUES ('ADMIN_EMAIL', 'den-0381@ya.ru');

INSERT INTO version (version) VALUES ('1.17');