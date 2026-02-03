package ru.mentee.power.crm.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mentee.power.crm.domain.DealStatus;
import ru.mentee.power.crm.spring.entity.Deal;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface JpaDealRepository extends JpaRepository<Deal, UUID> {

    Optional<Deal> findByLeadId(UUID leadId);
    List<Deal> findByStatus(DealStatus status);

}
