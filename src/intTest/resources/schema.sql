CREATE TABLE ballots (
    id SERIAL PRIMARY KEY,
    participant_id BIGINT NOT NULL,
    lottery_id BIGINT NOT NULL
);

CREATE TABLE lotteries (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL,
    finished BOOLEAN
);

CREATE TABLE participant (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

CREATE TABLE winner_ballots (
    ballot_id SERIAL PRIMARY KEY,
    lottery_id BIGINT NOT NULL,
    participant_id BIGINT NOT NULL,
    participant_name VARCHAR(255) NOT NULL,
    winning_date DATE NOT NULL
);