package ru.mentee.power.crm.spring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "invitees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invitee {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String email;

  private String firstName;

  private InviteeStatus status = InviteeStatus.INACTIVE;

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Invitee invitee)) return false;
    return Objects.equals(id, invitee.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
