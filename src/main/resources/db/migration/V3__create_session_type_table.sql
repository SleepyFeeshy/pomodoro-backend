create table session_types (
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