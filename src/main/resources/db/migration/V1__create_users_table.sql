CREATE TABLE users (
       id         UUID PRIMARY KEY,
       email      TEXT NOT NULL,
       created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    is_active bool
);

CREATE UNIQUE INDEX users_email_lower_idx ON users (lower(email));

INSERT INTO users (id, email, is_active) VALUES
    ('af24efdc-7387-4b9b-bd8c-bd183b393205', 'user@email.com', true);