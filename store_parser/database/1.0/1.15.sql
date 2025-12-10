ALTER TABLE application ADD migrated_data TEXT NULL;

INSERT INTO version (version) VALUES ('1.15');