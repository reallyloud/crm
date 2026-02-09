--liquibase formatted sql
--changeset your-name:BCORE-32-2

CREATE TABLE leads (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(50),
    company_id UUID REFERENCES companies(id),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    created_by UUID,
    assigned_to UUID,
    version BIGINT NOT NULL DEFAULT 0
);
-- Не забудьте: REFERENCES companies(id) для FK