package ru.mentee.power.crm.repository;

import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.repository.LeadRepository;

import java.util.*;

public class InMemoryLeadRepository implements LeadRepository<Lead> {
    private final Map<UUID, Lead> storageUUID = new HashMap<>();
    private final Map<String, UUID> storageEmail = new HashMap<>();

    public void save(Lead lead) {
        storageUUID.put(lead.id(), lead);
        storageEmail.put(lead.contact().email(), lead.id());
    }

    public Optional<Lead> findById(UUID id) {
        return Optional.ofNullable(storageUUID.get(id));
    }

    public Optional<Lead> findByEmail(String email) {
        UUID uuid = storageEmail.get(email);
        return Optional.ofNullable(storageUUID.get(uuid));
    }

    public List<Lead> findAll() {
        return storageUUID.values().stream().toList();
    }

    public void delete(UUID id) {
        String email = storageUUID.get(id).contact().email();
        storageUUID.remove(id);
        storageEmail.remove(email);
    }

    public int size() {
        return storageUUID.size();
    }
}
