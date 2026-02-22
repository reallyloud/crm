package ru.mentee.power.crm.spring.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.mentee.power.crm.domain.DealStatus;
import ru.mentee.power.crm.spring.entity.Deal;

public interface JpaDealRepository extends JpaRepository<Deal, UUID> {

  Optional<Deal> findByLeadId(UUID leadId);

  List<Deal> findByStatus(DealStatus status);

  @EntityGraph(attributePaths = {"dealProducts", "dealProducts.product"})
  @Query("SELECT d FROM Deal d WHERE d.id = :id")
  Optional<Deal> findDealWithProducts(UUID id);
}
