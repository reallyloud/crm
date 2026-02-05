package ru.mentee.power.crm.spring.service;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.spring.repository.JpaLeadRepository;
import ru.mentee.power.crm.testHelpClasses.DataGenerator;

@ActiveProfiles("test")
@SpringBootTest
class LeadLockingServiceTest {

    @Autowired
    private LeadLockingService leadLockingService;

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JpaLeadRepository leadRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private static void awaitBarrier(CyclicBarrier barrier) {
        try {
            barrier.await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldPreventLostUpdate_whenPessimisticLockUsed() throws Exception {
        // Given: Lead с начальным статусом
        Lead lead = DataGenerator.generateRandomLead();
        lead.setStatus(LeadStatus.NEW);

        lead = leadRepository.save(lead);
        UUID leadId = lead.getId();

        // When: Два потока одновременно обновляют Lead с pessimistic lock
        ExecutorService executor = Executors.newFixedThreadPool(2);

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(2);

        Future<LeadStatus> task1 = executor.submit(() -> {
            startLatch.await(); // Синхронизируем старт
            Lead updated = leadLockingService.convertLeadToDealWithLock(leadId, "CONTACTED");
            doneLatch.countDown();
            return updated.getStatus();
        });

        Future<LeadStatus> task2 = executor.submit(() -> {
            startLatch.await();
            Lead updated = leadLockingService.convertLeadToDealWithLock(leadId, "QUALIFIED");
            doneLatch.countDown();
            return updated.getStatus();
        });

        startLatch.countDown(); // Запускаем оба потока одновременно
        doneLatch.await(10, TimeUnit.SECONDS); // Ждём завершения

        // Then: Оба обновления успешны, вторая транзакция ждала первую
        LeadStatus status1 = task1.get();
        LeadStatus status2 = task2.get();

        assertThat(status1).isIn(LeadStatus.CONTACTED, LeadStatus.QUALIFIED);
        assertThat(status2).isIn(LeadStatus.CONTACTED, LeadStatus.QUALIFIED);
        assertThat(status1).isNotEqualTo(status2); // Разные статусы (не должны быть)

        // Финальный статус — последняя commit'нутая транзакция
        Lead finalLead = leadRepository.findById(leadId).orElseThrow();
        assertThat(finalLead.getStatus()).isIn(LeadStatus.CONTACTED, LeadStatus.QUALIFIED);

        executor.shutdown();
    }

    @Test
    void shouldThrowOptimisticLockException_whenConcurrentUpdateWithoutLock() throws Exception {
        // Given: Lead с optimistic locking через @Version
        Lead lead = DataGenerator.generateRandomLead();
        lead.setStatus(LeadStatus.NEW);

        lead = leadRepository.save(lead);
        UUID leadId = lead.getId();

        CyclicBarrier bothHaveRead = new CyclicBarrier(2);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch startLatch = new CountDownLatch(1);

        Future<?> task1 = executor.submit(() -> {
            startLatch.await();
            transactionTemplate.executeWithoutResult(__ -> {
                Lead l = leadRepository.findById(leadId).orElseThrow();
                awaitBarrier(bothHaveRead);
                l.setStatus(LeadStatus.CONTACTED);
                leadRepository.save(l);
            });
            return null;
        });

        Future<?> task2 = executor.submit(() -> {
            startLatch.await();
            transactionTemplate.executeWithoutResult(__ -> {
                Lead l = leadRepository.findById(leadId).orElseThrow();
                awaitBarrier(bothHaveRead);
                l.setStatus(LeadStatus.QUALIFIED);
                leadRepository.save(l);
            });
            return null;
        });

        startLatch.countDown();

        // Then: одна транзакция успешна, вторая выбрасывает OptimisticLockException
        boolean exceptionThrown = false;
        try {
            task1.get(5, TimeUnit.SECONDS);
            task2.get(5, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            assertThat(e.getCause())
                    .isInstanceOfAny(ObjectOptimisticLockingFailureException.class);
            exceptionThrown = true;
        }

        assertThat(exceptionThrown).isTrue();
        executor.shutdown();
    }

    @Test
    void shouldThrowDeadLockException() throws Exception {

        Lead lead1 = DataGenerator.generateRandomLead();
        Lead lead2 = DataGenerator.generateRandomLead();
        lead1.setStatus(LeadStatus.NEW);
        lead2.setStatus(LeadStatus.NEW);

        lead1 = leadRepository.save(lead1);
        lead2 = leadRepository.save(lead2);

        UUID leadId1 = lead1.getId();
        UUID leadId2 = lead2.getId();

        ExecutorService executor = Executors.newFixedThreadPool(2);

        CountDownLatch startLatch = new CountDownLatch(1);

        Future<?> task1 = executor.submit(() -> {
            startLatch.await();
            leadLockingService.lockTwoLeadsInOrder(leadId1, leadId2);
            log.info("Первый поток заблокировал оба лида");
            return null;
        });

        Future<?> task2 = executor.submit(() -> {
            startLatch.await();
            leadLockingService.lockTwoLeadsInOrder(leadId2, leadId1);
            log.info("Второй поток заблокировал оба лида");
            return null;
        });

        startLatch.countDown();

        boolean exceptionThrown = false;
        try {
            task1.get(5, TimeUnit.SECONDS);
            task2.get(5, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            // Одна из транзакций должна выбросить исключение из-за deadlock / lock timeout
            assertThat(e.getCause())
                    .isInstanceOfAny(CannotAcquireLockException.class);
            exceptionThrown = true;
        }

        assertThat(exceptionThrown).isTrue();
        executor.shutdown();
    }

    // TODO: Создайте тест для deadlock ситуации
    // - Два потока блокируют Lead'ы в разном порядке
    // - Один из потоков должен получить CannotAcquireLockException
}