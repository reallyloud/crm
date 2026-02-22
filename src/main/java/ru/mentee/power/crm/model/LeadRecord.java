package ru.mentee.power.crm.model;

import java.util.UUID;

public record LeadRecord(UUID id, String email, String company, String status) {
  // Record автоматически генерирует equals/hashCode по всем полям
  // HashMap сможет использовать id как ключ
}
