package ru.mentee.power.crm.spring.dto;

import java.util.UUID;
import ru.mentee.power.crm.spring.entity.InviteeStatus;

/**
 * DTO ответа для приглашённого пользователя. Содержит только публичные поля для API — без
 * внутренних деталей Entity.
 */
public record InviteeResponse(UUID id, String email, String firstName, InviteeStatus status) {}
