CREATE TABLE stream (
    id SERIAL PRIMARY KEY NOT NULL,
    started_at TIMESTAMP NOT NULL,
    end_at TIMESTAMP,
    max_viewers INTEGER
);

