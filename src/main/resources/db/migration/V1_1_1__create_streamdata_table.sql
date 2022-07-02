CREATE TABLE stream_data (
    id SERIAL PRIMARY KEY NOT NULL,
    game_name VARCHAR(100) NOT NULL,
    title VARCHAR(255) NOT NULL,
    viewer_count INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    stream_id SERIAL REFERENCES stream (id)
);