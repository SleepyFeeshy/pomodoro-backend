create table sessions (
     id uuid not null,
     user_id uuid not null,
     finished_at timestamp with time zone null,
     duration double precision null,
     updated_at timestamp with time zone null,
     session_type_id uuid null,
     deleted_at timestamp with time zone null,
     server_seq int,
     constraint sessions_pkey primary key (id),
     constraint sessions_user_id_fkey foreign KEY (user_id) references users (id),
     constraint sessions_session_type_id_fkey foreign KEY (session_type_id) references session_types (id)
);