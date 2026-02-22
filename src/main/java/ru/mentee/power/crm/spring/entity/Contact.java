package ru.mentee.power.crm.spring.entity;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "contacts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Contact {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @JoinColumn(name = "lead_id")
  private UUID leadId;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String phone;

  @Column(nullable = false)
  private String address;

  @Version
  @Setter(AccessLevel.NONE)
  private Long version;

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Contact contact)) return false;
    return Objects.equals(id, contact.id)
        && Objects.equals(email, contact.email)
        && Objects.equals(phone, contact.phone)
        && Objects.equals(address, contact.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email, phone, address);
  }
}
