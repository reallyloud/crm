package ru.mentee.power.crm.spring.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.domain.DealStatus;
import ru.mentee.power.crm.spring.entity.Deal;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.repository.JpaLeadRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JpaLeadService {

    private static final Logger log = LoggerFactory.getLogger(JpaLeadService.class);

    private final JpaLeadRepository repository;
    private final JpaLeadProcessor processor;

    /**
     * Поиск лида по email (derived method).
     */
    public Optional<Lead> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    /**
     * Поиск лидов по списку статусов (JPQL).
     */
    public List<Lead> findByStatuses(LeadStatus... statuses) {
        return repository.findByStatusIn(List.of(statuses));
    }

    /**
     * Получить первую страницу лидов с сортировкой.
     */
    public Page<Lead> getFirstPage(int pageSize) {
        PageRequest pageRequest = PageRequest.of(
                0, // первая страница (нумерация с 0)
                pageSize,
                Sort.by("createdAt").descending()
        );
        return repository.findAll(pageRequest);
    }

    public Page<Lead> searchByCompany(String company, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByCompany(company, pageable);
    }

    /**
     * Массовое обновление статуса (используется @Modifying метод).
     * ВАЖНО: @Transactional обязательна для @Modifying!
     */
    @Transactional
    public int convertNewToContacted() {
        int updated = repository.updateStatusBulk(LeadStatus.NEW, LeadStatus.CONTACTED);
        // Логируем для observability
        log.info("Converted {} leads from NEW to CONTACTED",updated);
        return updated;
    }

    @Transactional
    public int archiveOldLeads(LeadStatus status) {
    return repository.deleteByStatusBulk(status);
    }

    // SELF INVOKE PROBLEM
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void processSingleLead(UUID id,int i) {
        if (i == 1) {
            throw new IllegalArgumentException();
        }
        if (repository.findById(id).isPresent()) {
            Lead lead = repository.findById(id).get();
            lead.setName("PROCESSED");
            repository.save(lead);
        }
    }

    // SELF INVOKE PROBLEM
    public void processLeadsSelfInvoke(List<UUID> ids) {
        int i = 0;
        for (UUID id: ids) {
            this.processSingleLead(id,i);
            i++;
        }
    }

    public void processLeads(List<UUID> ids) {
        int i = 0;
        for (UUID id: ids) {
            processor.processSingleLead(id,i);
            i++;
        }

    }







}
