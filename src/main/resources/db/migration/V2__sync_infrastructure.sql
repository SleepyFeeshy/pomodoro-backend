-- Global change counter: every insert/update on a synced table takes the next value
CREATE SEQUENCE change_seq;

-- Stamps server_seq on every insert/update, so pull sees the change in server order
CREATE FUNCTION bump_seq() RETURNS trigger AS $$
BEGIN
    NEW.server_seq := nextval('change_seq');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Idempotency: remembers which client mutations were already applied
CREATE TABLE processed_mutations (
     mutation_id  UUID PRIMARY KEY,
     user_id      UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
     processed_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX processed_mutations_processed_at_idx ON processed_mutations (processed_at);