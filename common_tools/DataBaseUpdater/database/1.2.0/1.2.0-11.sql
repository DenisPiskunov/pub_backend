CREATE TABLE payment_card
(
    id                        SERIAL,
    card_number               VARCHAR(16),
    card_holder               VARCHAR(100),
    exp_month                 CHAR(2),
    exp_year                  INT,
    cvc                       CHAR(3),
    phone_number              CHAR(11),
    description               TEXT,
    payment_system_id         BIGINT,
    payment_system_account_id BIGINT,
    is_deleted                BOOLEAN     DEFAULT FALSE NOT NULL,
    creation_date             TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    
    CONSTRAINT pk_payment_card PRIMARY KEY (id),

    CONSTRAINT fk_payment_card_payment_system_id FOREIGN KEY (payment_system_id) REFERENCES payment_system (id),
    
    CONSTRAINT fk_payment_card_payment_system_account_id FOREIGN KEY (payment_system_account_id) REFERENCES payment_system_account (id),
    
    CONSTRAINT chk_payment_system_id_payment_system_account_id CHECK (
            (payment_system_id IS NULL AND payment_system_account_id IS NOT NULL) OR (payment_system_id IS NOT NULL AND payment_system_account_id IS NULL)),
    
    CONSTRAINT chk_card_info_phone_number CHECK (
            (card_number IS NOT NULL AND card_holder IS NOT NULL AND exp_month IS NOT NULL AND exp_year IS NOT NULL AND cvc IS NOT NULL AND phone_number IS NULL) OR
            (card_number IS NULL AND card_holder IS NULL AND exp_month IS NULL AND exp_year IS NULL AND cvc IS NULL AND phone_number IS NOT NULL)
        ),
    CONSTRAINT chk_card_number CHECK ( length(card_number) = 16 ),

    CONSTRAINT chk_exp_month CHECK (exp_month BETWEEN '01' AND '12'),

    CONSTRAINT chk_exp_year CHECK (exp_year BETWEEN 2020 AND 2100),
    
    CONSTRAINT chk_cvc CHECK ( length(cvc) = 3 )
);

ALTER TYPE DB_OBJECT ADD VALUE 'PAYMENT_CARD';

INSERT INTO version (version)
VALUES ('1.2.0-11');