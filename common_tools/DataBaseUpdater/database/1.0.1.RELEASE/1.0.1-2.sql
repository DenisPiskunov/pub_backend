CREATE TABLE fb_accounts
(
    id             SERIAL NOT NULL,
    server_name    VARCHAR,
    application    VARCHAR,
    platform_info  VARCHAR,
    privacy_policy VARCHAR,
    access_data    VARCHAR,
    email_data     VARCHAR,
    status         VARCHAR,

    CONSTRAINT pk_fb_accounts PRIMARY KEY (id)
);

INSERT INTO version (version)
VALUES ('1.0.1-2');