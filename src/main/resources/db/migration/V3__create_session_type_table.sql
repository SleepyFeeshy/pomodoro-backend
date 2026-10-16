create table session_types (
      id uuid not null,
      label text not null,
      default_duration integer null,
      is_default boolean null default false,
      created_at timestamp with time zone null default CURRENT_TIMESTAMP,
      updated_at timestamp with time zone null default CURRENT_TIMESTAMP,
      synced_at timestamp with time zone null,
      deleted_at timestamp with time zone null,
      server_seq int,
      constraint session_types_pkey primary key (id),
      constraint session_types_label_key unique (label)
);

INSERT INTO session_types (id, label, default_duration, is_default) VALUES
    ('550e8400-e29b-41d4-a716-446655440000', 'Work', 20, true),
    ('6ba7b810-9dad-11d1-80b4-00c04fd430c8', 'Short Break', 5, true),
    ('6ba7b811-9dad-11d1-80b4-00c04fd430c8', 'Long Break', 15, true);