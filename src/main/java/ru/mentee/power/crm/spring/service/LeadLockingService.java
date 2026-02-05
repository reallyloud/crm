package ru.mentee.power.crm.spring.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.OptimisticLockException;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.spring.repository.JpaLeadRepository;

@Service
public class LeadLockingService {

    Logger log = LoggerFactory.getLogger(LeadLockingService.class);
    private final JpaLeadRepository leadRepository;

    public LeadLockingService(JpaLeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    // Критическая операция с pessimistic lock
    @Transactional
    public Lead convertLeadToDealWithLock(UUID leadId, String newStatus) {
        // Блокируем Lead эксклюзивно до конца транзакции
        Lead lead = leadRepository.findByIdForUpdate(leadId)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + leadId));

        // Здесь могла бы быть сложная бизнес-логика конверсии:
        // - создание Deal
        // - обновление статуса Lead
        // - отправка уведомлений
        // Другие транзакции ЖДУТ завершения этой операции

        lead.setStatus(LeadStatus.valueOf(newStatus));
        return leadRepository.save(lead);
    }

    // Обычное обновление с optimistic lock (через @Version)
    @Transactional
    public Lead updateLeadStatusOptimistic(UUID leadId, LeadStatus newStatus) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + leadId));

        // Блокировки НЕТ — другие транзакции могут читать и изменять
        // При сохранении JPA проверит version и выбросит OptimisticLockException если конфликт

        lead.setStatus(newStatus);
        return leadRepository.save(lead);
        // UPDATE leads SET status=?, version=version+1 WHERE id=? AND version=?
    }



    /**
     * Блокирует два лида в заданном порядке (в одной транзакции).
     * Используется для теста deadlock: один поток вызывает lockTwoLeads(id1, id2),
     * другой — lockTwoLeads(id2, id1).
     */
    @Transactional
    public void lockTwoLeadsInOrder(UUID firstId, UUID secondId) {
        leadRepository.findByIdForUpdate(firstId).orElseThrow(() -> new IllegalArgumentException("Lead not found: " + firstId));
        leadRepository.findByIdForUpdate(secondId).orElseThrow(() -> new IllegalArgumentException("Lead not found: " + secondId));
    }

    // TODO: Создайте метод для демонстрации обработки OptimisticLockException
    @Transactional
    public Lead updateWithRetry(UUID leadId, LeadStatus newStatus) {
        Lead result = null;
        try {
            result = updateLeadStatusOptimistic(leadId, newStatus);
        } catch (OptimisticLockException e) {
            log.info("При запросе возник OptimisticLockException, повторяем операцию.");
            updateWithRetry(leadId,newStatus);
            // Логируем конфликт
            // Можно повторить операцию или вернуть ошибку
        }
        return result;
    }

}
