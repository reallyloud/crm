--liquibase formatted sql
--changeset your-name:BCORE-32-3

CREATE TABLE contacts (
    id UUID PRIMARY KEY,
    lead_id UUID REFERENCES leads(id) ON DELETE CASCADE,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    address VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE
);
