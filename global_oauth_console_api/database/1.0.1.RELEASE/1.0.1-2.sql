TRUNCATE account CASCADE ;
TRUNCATE role RESTART IDENTITY CASCADE ;
TRUNCATE authority RESTART IDENTITY CASCADE;

INSERT INTO authority (name)
VALUES ('ROLE_READ');
INSERT INTO authority (name)
VALUES ('ROLE_CREATE');
INSERT INTO authority (name)
VALUES ('ROLE_UPDATE');
INSERT INTO authority (name)
VALUES ('ROLE_DELETE');

INSERT INTO authority (name)
VALUES ('MASTER_ACCOUNT_READ');
INSERT INTO authority (name)
VALUES ('MASTER_ACCOUNT_CREATE');
INSERT INTO authority (name)
VALUES ('MASTER_ACCOUNT_UPDATE');
INSERT INTO authority (name)
VALUES ('MASTER_ACCOUNT_DELETE');
INSERT INTO authority (name)
VALUES ('MASTER_ACCOUNT_BLOCK');


INSERT INTO authority (name)
VALUES ('ACCOUNT_READ');
INSERT INTO authority (name)
VALUES ('ACCOUNT_CREATE');
INSERT INTO authority (name)
VALUES ('ACCOUNT_UPDATE');

INSERT INTO role (name)
VALUES ('ADMIN');

INSERT INTO role_to_authority (role_id, authority_id)
SELECT r.id, auth.id
FROM role AS r,
     authority AS auth;

INSERT INTO account (uuid)
VALUES ('b2ed3290-388e-4584-a64f-209c030a648b');

INSERT INTO account_to_role (account_uuid, role_id)
SELECT acc.uuid, r.id
FROM account AS acc,
     role AS r;

INSERT INTO version (version)
VALUES ('1.0.1-2');