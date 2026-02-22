package ru.mentee.power.crm.spring.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mentee.power.crm.model.LeadStatus;

@Entity
@Table(name = "leads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lead {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Version
  @Setter(AccessLevel.NONE)
  private Long version;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String phone;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "company_id")
  private Company company;

  @Enumerated(EnumType.STRING)
  private LeadStatus status;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  public Lead(String name, String email, Company company, LeadStatus status) {
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
    return Objects.equals(id, lead.id)
        && Objects.equals(email, lead.email)
        && Objects.equals(company, lead.company)
        && Objects.equals(phone, lead.phone)
        && Objects.equals(status, lead.status)
        && Objects.equals(createdAt, lead.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email, company, phone, status, createdAt);
  }

  // Lombok автоматически генерирует: геттеры, сеттеры, equals, hashCode, toString, конструкторы
}
