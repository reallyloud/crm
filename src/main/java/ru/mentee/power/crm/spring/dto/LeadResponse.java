package ru.mentee.power.crm.spring.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import ru.mentee.power.crm.model.LeadStatus;

public record LeadResponse(
    UUID id, String name, String email, String phone, LeadStatus status, LocalDateTime createdAt) {}
