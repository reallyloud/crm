package ru.mentee.power.crm.spring.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.mentee.power.crm.model.LeadStatus;

public record LeadForm(
        @NotBlank(message = "Имя обязательно")
        String name,

        @NotBlank(message = "Email обязателен")
        @Email(message = "Некорректный формат email")
        String email,

        @NotBlank(message = "Телефон обязателен")
        String phone,

        String companyName,

        @NotNull(message = "Статус обязателен")
        LeadStatus status
) {
}
