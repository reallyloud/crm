package ru.mentee.power.crm.spring.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ru.mentee.power.crm.model.LeadStatus;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "leads")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String company;

    @Enumerated(EnumType.STRING)
    private LeadStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Lead(String name,String email, String company, LeadStatus status) {
        this.name = name;
        this.email = email;
        this.company = company;
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Lead lead)) return false;
        return
                Objects.equals(id, lead.id) &&
                Objects.equals(email, lead.email) &&
                Objects.equals(company, lead.company) &&
                Objects.equals(phone, lead.phone) &&
                Objects.equals(status, lead.status) &&
                Objects.equals(createdAt, lead.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, company, phone, status, createdAt);
    }

    // Lombok автоматически генерирует: геттеры, сеттеры, equals, hashCode, toString, конструкторы
}