create table if not exists customers (
    id bigserial not null,
    name varchar not null,
    email varchar not null,
    primary key (id),
    UNIQUE (email)
);

CREATE TABLE ballots (
    id SERIAL PRIMARY KEY,
    participant_id BIGINT NOT NULL,
    lottery_id BIGINT NOT NULL
);

CREATE TABLE lotteries (
    id SERIAL PRIMARY KEY,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    finished BOOLEAN
);

CREATE TABLE participant (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);