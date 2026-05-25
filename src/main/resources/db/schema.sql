-- Run this manually on your Supabase instance if the user table does not exist yet.
-- If the product-service schema is already applied, this table already exists.

CREATE TABLE IF NOT EXISTS "user" (
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    phone         VARCHAR(50),
    password_hash VARCHAR(255) NOT NULL
);
