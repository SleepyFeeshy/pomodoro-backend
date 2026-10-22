CREATE TABLE users (
       id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
       email      TEXT UNIQUE NOT NULL,
       created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    is_active bool NOT NULL DEFAULT true
);

CREATE TABLE user_auth_credentials (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL references users(id),
    provider TEXT NOT NULL,
    provider_user_id TEXT NOT NULL,
    identifier TEXT NOT NULL,
    password_hash TEXT,
    last_login_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT provider_user_unique UNIQUE (provider, provider_user_id)
);

CREATE UNIQUE INDEX users_email_lower_idx ON users (lower(email));

CREATE TABLE roles (
    id UUID PRIMARY KEY DEFAULT  gen_random_uuid(),
    role_name TEXT UNIQUE NOT NULL
);

CREATE TABLE user_roles (
    id UUID PRIMARY KEY DEFAULT  gen_random_uuid(),
    user_id UUID NOT NULL references users(id),
    role_id UUID NOT NULL references roles(id),
    CONSTRAINT user_role_unique UNIQUE (user_id, role_id)
);

INSERT INTO users (id, email, is_active) VALUES
    ('af24efdc-7387-4b9b-bd8c-bd183b393205', 'user@email.com', true);