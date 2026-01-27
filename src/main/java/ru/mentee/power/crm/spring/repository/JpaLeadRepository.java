package ru.mentee.power.crm.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.entity.Lead;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaLeadRepository extends JpaRepository<Lead, UUID> {

    @Query(value = "SELECT * FROM leads WHERE email = ?1",nativeQuery = true)
    public Optional<Lead> findByEmailNative(String email);

    @Query(value = "SELECT * FROM leads WHERE status = ?1" ,nativeQuery = true)
    public List<Lead> findByStatusNative(LeadStatus status);

    // для поиска по email
}