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