CREATE SCHEMA IF NOT EXISTS wm;

SET SCHEMA wm;

CREATE SEQUENCE IF NOT EXISTS wallet_id_seq;

CREATE TABLE IF NOT EXISTS wallet
(
    id
    BIGINT
    PRIMARY
    KEY,
    balance
    DECIMAL
(
    19,
    2
) NOT NULL
    );

CREATE SEQUENCE IF NOT EXISTS top_up_id_seq;

CREATE TABLE IF NOT EXISTS top_up
(
    id
    BIGINT
    PRIMARY
    KEY,
    amount
    DECIMAL
(
    19,
    2
) NOT NULL,
    payment_id VARCHAR
(
    255
) NOT NULL,
    wallet_id BIGINT NOT NULL,
    FOREIGN KEY
(
    wallet_id
) REFERENCES wallet
(
    id
)
    );