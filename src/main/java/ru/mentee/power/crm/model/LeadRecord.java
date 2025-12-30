package ru.mentee.power.crm.model;

public record LeadRecord(
        String id,
        String email,
        String phone,
        String company,
        String status
) {
    // Record автоматически генерирует equals/hashCode по всем полям
    // HashMap сможет использовать id как ключ
}