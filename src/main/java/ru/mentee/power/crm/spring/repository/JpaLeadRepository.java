package ru.mentee.power.crm.spring.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.entity.Company;
import ru.mentee.power.crm.spring.entity.Lead;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaLeadRepository extends JpaRepository<Lead, UUID> {

    Page<Lead> findByStatus(LeadStatus status, Pageable pageable);

    Page<Lead> findByCompany(String company, Pageable pageable);

    Optional<Lead> findByEmail(String email);

    List<Lead> findByStatus(LeadStatus status);

    List<Lead> findByEmailContaining(String emailPart);

    @EntityGraph(attributePaths = ("company"))
    List<Lead> findByCompany(Company company);

    Optional<Lead> findByEmailIgnoreCase(String email);

    long countByStatus(LeadStatus status);

    boolean existsByEmail(String email);

    List<Lead> findByStatusAndCompanyName(LeadStatus status, String companyName);

    List<Lead> findByStatusOrderByCreatedAtDesc(LeadStatus status);

    //JPQL запросы:
    @Query("SELECT l.id FROM Lead l")
    List<UUID> findAllIds();

    @Query("SELECT l FROM Lead l WHERE l.status IN :statuses")
    public List<Lead> findByStatusIn(@Param("statuses") List<LeadStatus> statuses);

    @Query("SELECT l FROM Lead l WHERE l.createdAt > :date")
    List<Lead> findCreatedAfter(@Param("date") LocalDateTime date);

    @Query("SELECT l FROM Lead l WHERE l.company =:companyName")
    List<Lead> findByCompanyName(@Param("companyName") String name);

    //Возвращает количество ОБНОВЛЁННЫХ строк
    @Modifying
    @Query("UPDATE Lead l SET l.status = :newStatus WHERE l.status = :oldStatus")
    int updateStatusBulk(
            @Param("oldStatus") LeadStatus oldStatus,
            @Param("newStatus") LeadStatus newStatus
    );

    @Modifying
    @Query("UPDATE Lead l SET l.phone = :newPhone WHERE l.company.id = :companyId")
    int updatePhoneBulk(
            @Param("companyId") UUID companyId,
            @Param("newPhone") String phone
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Lead l WHERE l.id = :id")
    Optional<Lead> findByIdForUpdate(@Param("id") UUID id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Lead l WHERE l.email = :email")
    Optional<Lead> findByEmailForUpdate(@Param("email") String email);


    //Возвращает количество УДАЛЁННЫХ строк
    @Modifying
    @Query("DELETE FROM Lead l WHERE l.status = :status")
    int deleteByStatusBulk(@Param("status") LeadStatus status);


}