package ru.mentee.power.crm.spring.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeadRepository<Lead> {
  public Lead save(Lead type);

  public void delete(UUID uuid);

  public Optional<Lead> findById(UUID id);

  public List<Lead> findAll();

  public Optional<Lead> findByEmail(String email);
}
