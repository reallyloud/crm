package ru.mentee.power.crm.infrastructure;

import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.domain.Repository;

import java.util.*;

public class InMemoryLeadRepository implements Repository<Lead> {
    private final List<Lead> storage = new ArrayList<>();

    @Override
    public void add(Lead lead) {
        if (!storage.contains(lead)) {
            storage.add(lead);
        }
    }

    @Override
    public void remove(UUID id) {
        for (Lead lead : storage) {
            if (lead.id().equals(id)) {
                storage.remove(lead);
                break;
            }
        }
    }

    @Override
    public Optional<Lead> findById(UUID id) {
        for (Lead lead : storage) {
            if (lead.id().equals(id)) {
                return Optional.of(lead);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Lead> findAll() {
        return new ArrayList<>(storage);

    }

}
