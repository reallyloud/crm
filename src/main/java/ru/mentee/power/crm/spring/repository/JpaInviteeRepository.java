package ru.mentee.power.crm.spring.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.mentee.power.crm.spring.entity.Invitee;

/** Spring Data JPA репозиторий для Invitee. Использует derived methods — без SQL injection. */
public interface JpaInviteeRepository extends JpaRepository<Invitee, UUID> {

  boolean existsByEmail(String email);
}
