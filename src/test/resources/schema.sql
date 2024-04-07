CREATE SCHEMA IF NOT EXISTS wm;

SET SCHEMA wm;

DROP SEQUENCE IF EXISTS top_up_id_seq;
DROP TABLE IF EXISTS top_up;
DROP SEQUENCE IF EXISTS wallet_id_seq;
DROP TABLE IF EXISTS wallet;


CREATE SEQUENCE wallet_id_seq;

CREATE TABLE wallet
(
    id      BIGINT PRIMARY KEY,
    balance DECIMAL(19, 2) NOT NULL
);

CREATE SEQUENCE top_up_id_seq;

CREATE TABLE top_up
(
    id         BIGINT PRIMARY KEY,
    amount     DECIMAL(19, 2) NOT NULL,
    payment_id VARCHAR(255)   NOT NULL,
    wallet_id  BIGINT         NOT NULL,
    FOREIGN KEY (wallet_id) REFERENCES wallet (id)
);