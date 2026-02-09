--liquibase formatted sql
--changeset your-name:BCORE-32-1

-- Поля (из Company.java entity):
CREATE TABLE companies (
    id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    industry VARCHAR(100)
);