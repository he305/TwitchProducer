CREATE TABLE IF NOT EXISTS streamer (
    id SERIAL PRIMARY KEY NOT NULL,
    nickname TEXT NOT NULL UNIQUE,
    PRIMARY KEY (id)
);