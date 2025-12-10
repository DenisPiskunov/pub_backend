CREATE TABLE accounts_and_servers
(
    id                        SERIAL NOT NULL,
    server_name               VARCHAR,
    hosting_data              VARCHAR,
    date_next_renewal         VARCHAR,
    connection_data           VARCHAR,
    hosting_email             VARCHAR,
    account_email_data        VARCHAR,
    app_gallery               VARCHAR,
    appstore                  VARCHAR,
    google_play               VARCHAR,
    domain_email              VARCHAR,
    domain_email_next_renewal VARCHAR,
    site_data                 VARCHAR,
    site_data_next_renewal    VARCHAR,
    domain_data               VARCHAR,
    domain_data_next_renewal  VARCHAR,

    CONSTRAINT pk_account_and_servers PRIMARY KEY (id)
);


INSERT INTO version (version)
VALUES ('1.0.1-1');