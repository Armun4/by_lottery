CREATE TABLE winner_ballots (
    ballot_id SERIAL PRIMARY KEY,
    lottery_id BIGINT NOT NULL,
    participant_id BIGINT NOT NULL,
    participant_name VARCHAR(255) NOT NULL,
    winning_date DATE NOT NULL
);