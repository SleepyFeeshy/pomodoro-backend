CREATE TABLE IF NOT EXISTS session_type (
          id uuid not null,
          label text not null,
          default_duration integer null,
          is_default boolean null default false,
          created_at timestamp with time zone null default CURRENT_TIMESTAMP,
          updated_at timestamp with time zone null default CURRENT_TIMESTAMP,
          synced_at timestamp with time zone null,
          deleted_at timestamp with time zone null,
          constraint session_types_pkey primary key (id),
          constraint session_types_label_key unique (label)
);

CREATE TABLE IF NOT EXISTS sessions (
         id uuid not null,
         finished_at timestamp with time zone null,
         duration double precision null,
         updated_at timestamp with time zone null,
         session_type_id uuid null,
         deleted_at timestamp with time zone null,
         constraint sessions_pkey primary key (id),
         constraint sessions_session_type_id_fkey foreign KEY (session_type_id) references session_type (id)
);