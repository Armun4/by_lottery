CREATE TABLE ballots (
    id SERIAL PRIMARY KEY,
    participant_id BIGINT NOT NULL,
    lottery_id BIGINT NOT NULL
);