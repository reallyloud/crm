package ru.mentee.power.crm.repository;

import ru.mentee.power.crm.domain.Lead;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LeadRepository {
    private final Map<UUID, Lead> storage = new HashMap<>();

    public void save(Lead lead) {
        storage.put(lead.id(),lead);
    }

    public Lead findById(UUID id) {
        return storage.get(id);
    }

    public List<Lead> findAll() {

        return storage.values().stream().toList();
    }

    public void delete(UUID id) {
        storage.remove(id);
    }

    public int size() {
        return storage.size();
    }
}
