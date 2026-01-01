package ru.mentee.power.crm.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeadRepository<Type> {
    public void save(Type type);
    public void delete(UUID uuid);
    public Optional<Type> findById(UUID id);
    public List<Type> findAll();
}
