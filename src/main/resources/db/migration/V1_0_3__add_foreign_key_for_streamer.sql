DELETE FROM streamer;
ALTER TABLE streamer
ADD COLUMN person_id SERIAL REFERENCES person (id);