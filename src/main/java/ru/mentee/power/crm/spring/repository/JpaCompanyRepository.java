package ru.mentee.power.crm.spring.repository;


import ru.mentee.power.crm.spring.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;
import java.util.Optional;

public interface JpaCompanyRepository extends JpaRepository<Company, UUID> {


    // Метод для решения N+1 проблемы:
    @EntityGraph(attributePaths = {"leads"})
    @Query("SELECT c FROM Company c WHERE c.id = :id")
    Optional<Company> findByIdWithLeads(@Param("id") UUID id);

    Optional<Company> findByName(String name);
}