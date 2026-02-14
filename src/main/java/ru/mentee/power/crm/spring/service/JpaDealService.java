package ru.mentee.power.crm.spring.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.domain.DealStatus;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.entity.Deal;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.spring.repository.JpaDealRepository;
import ru.mentee.power.crm.spring.repository.JpaLeadRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JpaDealService {

    public final JpaDealRepository dealRepository;
    public final JpaLeadRepository leadRepository;

    private static final Logger log = LoggerFactory.getLogger(JpaDealService.class);

    @Transactional(propagation = Propagation.REQUIRED)
    public Deal convertLeadToDeal(UUID leadId, BigDecimal amount) {
        if (leadRepository.findById(leadId).isEmpty()) {
            throw new IllegalArgumentException("Лида с таким id не существует.");
        }
        if (amount == null) {
            throw new IllegalArgumentException("amount не может быть == null.");
        }

        Deal deal = new Deal(
                "title is empty",
                leadId,
                amount,
                DealStatus.NEW
        );

        dealRepository.save(deal);
        return deal;
    }

    // ========== CRUD methods for JpaDealController ==========

    public List<Deal> getAllDeals() {
        return dealRepository.findAll();
    }

    public Optional<Deal> findById(UUID id) {
        return dealRepository.findById(id);
    }

    public Map<DealStatus, List<Deal>> getDealsByStatusForKanban() {
        return dealRepository.findAll().stream()
                .collect(Collectors.groupingBy(Deal::getStatus));
    }

    @Transactional
    public Deal transitionDealStatus(UUID dealId, DealStatus newStatus) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new IllegalArgumentException("Deal not found: " + dealId));
        deal.transitionTo(newStatus);
        return dealRepository.save(deal);
    }

}
