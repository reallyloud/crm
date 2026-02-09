--liquibase formatted sql
--changeset your-name:BCORE-32-4

create TABLE deals (
      id UUID PRIMARY KEY,
      lead_id UUID REFERENCES leads(id) ON DELETE CASCADE,
      title VARCHAR(255) NOT NULL,
      status VARCHAR(50) NOT NULL,
      amount DECIMAL(15, 2) NOT NULL,
      created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP WITH TIME ZONE
)
