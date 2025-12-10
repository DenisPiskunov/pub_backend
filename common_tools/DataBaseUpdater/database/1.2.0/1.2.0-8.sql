ALTER TYPE DB_OBJECTS RENAME TO db_object;

ALTER TABLE payment_system_accounts
    RENAME TO payment_system_account;

ALTER TABLE payment_system_account
    ADD payment_system_id BIGINT NOT NULL;

TRUNCATE TABLE payment_system_account;

ALTER TABLE payment_system_account
    ADD CONSTRAINT fk_payment_system_account_payment_system_id FOREIGN KEY (payment_system_id) REFERENCES payment_system (id);


INSERT INTO version (version)
VALUES ('1.2.0-8');