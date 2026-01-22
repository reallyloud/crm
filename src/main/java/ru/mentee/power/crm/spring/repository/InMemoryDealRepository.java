package ru.mentee.power.crm.spring.repository;

import org.springframework.stereotype.Repository;
import ru.mentee.power.crm.domain.Deal;
import ru.mentee.power.crm.domain.DealStatus;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryDealRepository implements DealRepository {
    private final Map<UUID, Deal> storage = new ConcurrentHashMap<>();

    @Override
    public void save(Deal deal) {
        storage.put(deal.getId(),deal);
    }

    @Override
    public Optional<Deal> findById(UUID id) {
        Deal deal = storage.get(id);
        return Optional.ofNullable(deal);
    }

    @Override
    public List<Deal> findAll() {
        return storage.values().stream()
                .toList();
    }

    @Override
    public List<Deal> findByStatus(DealStatus status) {
        return storage.values().stream()
                .filter(deal -> deal.getStatus().equals(status))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}
