package ru.mentee.power.crm.spring.service;

import org.springframework.stereotype.Service;
import ru.mentee.power.crm.domain.Deal;
import ru.mentee.power.crm.domain.DealStatus;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.spring.repository.DealRepository;
import ru.mentee.power.crm.spring.repository.LeadRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DealService {
    private final DealRepository dealRepository;
    private final LeadRepository<Lead> leadRepository;

    public DealService(DealRepository dealRepository, LeadRepository<Lead> leadRepository) {
        this.dealRepository = dealRepository;
        this.leadRepository = leadRepository;
    }

    public Deal convertLeadToDeal(UUID leadId, BigDecimal amount) {
        if (leadRepository.findById(leadId).isEmpty()) {
            throw new IllegalArgumentException("Lead not found: " + leadId);
        }
        Deal deal = new Deal(leadId,amount);
        dealRepository.save(deal);
        return deal;
    }

    public Deal transitionDealStatus(UUID dealId, DealStatus newStatus) {
        if (dealRepository.findById(dealId).isEmpty()) {
            throw new IllegalArgumentException("Deal not found: " + dealId);
        }
        Deal deal = dealRepository.findById(dealId).get();
        deal.transitionTo(newStatus);
        dealRepository.save(deal);
        return deal;
    }

    public List<Deal> getAllDeals() {
        return dealRepository.findAll();
    }

    public Map<DealStatus, List<Deal>> getDealsByStatusForKanban() {
        return dealRepository.findAll().stream()
                .collect(Collectors.groupingBy(Deal::getStatus));
    }
}
