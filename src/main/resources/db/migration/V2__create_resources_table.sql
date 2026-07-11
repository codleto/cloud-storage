CREATE TABLE resources (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,

    parent_path VARCHAR(1024) NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('FILE', 'DIRECTORY')),

    size BIGINT,
    object_key VARCHAR(1024),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_resources_user_parent_name_type
        UNIQUE (user_id, parent_path, name, type)
);