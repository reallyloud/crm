package ru.mentee.power.crm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Objects;
@Entity
@Table(name = "leads")
public record Lead(

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        UUID id,

        Contact contact,

        @Column(name = "company")
        String company,


        @NotNull(message = "Статус обязателен")
        @Column(name = "status")
        LeadStatus status,


        @NotBlank(message = "Email обязателен")
        @Email(message = "Некорректный формат email")
        @Column(name = "email")
        String email,

        @NotBlank(message = "Телефон обязателен")
        String phone,

        @NotBlank(message = "Имя обязательно")
        String name,

        @Column(name = "created_at", nullable = false, updatable = false)
        LocalDateTime createdAt
) {
    public Lead {
    }


    public Lead(UUID id, Contact contact, String company, LeadStatus status) {
        if (status == null || company == null) {
            throw new IllegalArgumentException();
        }
        if (!(status == LeadStatus.NEW ||
                status == LeadStatus.QUALIFIED ||
                status == LeadStatus.CONVERTED ||
                status == LeadStatus.CONTACTED)) {
            throw new IllegalArgumentException();
        }

        this(id, contact, company, status, "NoEmail", "NoPhone", "NoName",LocalDateTime.now());
    }

    public Lead(UUID uuid, String email, String company, LeadStatus status) {
        if (email == null || company == null || status == null) {
            throw new IllegalArgumentException();
        }
        Address address = new Address("", "", "");
        Contact contact = new Contact("", "", address);

        this(uuid, contact, company, status, email, "NoPhone", "NoName",LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Lead lead = (Lead) o;
        return Objects.equals(id, lead.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
