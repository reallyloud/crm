package ru.mentee.power.crm.spring.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.mentee.power.crm.spring.Application;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.spring.repository.JpaLeadRepository;
import ru.mentee.power.crm.testHelpClasses.DataGenerator;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
class IsolationsTest {

    private final String masterTransaction = " {ОСНОВНАЯ ТРАНЗАКЦИЯ} ";
    private final String otherTransaction = " {ДРУГАЯ ТРАНЗАКЦИЯ} ";
    private String transactionName;

    @Autowired
    private JpaLeadRepository repository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private EntityManager entityManager;

    Logger log = LoggerFactory.getLogger(IsolationsTest.class);

    private UUID leadId;

    @BeforeEach
    @Transactional
    void setUp() {
        repository.deleteAll();
        Lead lead = DataGenerator.generateRandomLead();
        lead.setName("Старое имя");
        Lead savedLead = repository.save(lead);
        repository.flush();
        leadId = savedLead.getId();
        log.info("Инициализирован Lead с ID: {}", leadId);
    }

    @Test
    public void showDifference() {
        log.info("Тест с Isolation.READ_COMMITTED:");
        String[] readCommittedIsolation = testIsolationLevel(Isolation.READ_COMMITTED, leadId);
        String firstRead = readCommittedIsolation[0];
        String secondRead = readCommittedIsolation[1];

        log.info("READ_COMMITTED - Первое чтение: {}, Второе чтение: {}", firstRead, secondRead);
        // Проверяем результаты READ_COMMITTED. Должны быть РАЗНЫЕ значения.
        assertThat(firstRead).isNotEqualTo(secondRead);

        // Сбрасываем имя обратно
        resetLeadName();

        // Теперь REPEATABLE_READ
        log.info("Тест с Isolation.REPEATABLE_READ:");
        String[] repeatableReadIsolation = testIsolationLevel(Isolation.REPEATABLE_READ, leadId);
        firstRead = repeatableReadIsolation[0];
        secondRead = repeatableReadIsolation[1];

        log.info("REPEATABLE_READ - Первое чтение: {}, Второе чтение: {}", firstRead, secondRead);
        // Проверяем результаты REPEATABLE_READ. Должны быть ОДИНАКОВЫЕ значения.
        assertThat(firstRead).isEqualTo(secondRead);
    }

    @Transactional
    private void resetLeadName() {
        Lead lead = repository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead not found"));
        lead.setName("Старое имя");
        repository.save(lead);
        repository.flush();
        log.info("Имя Lead сброшено на 'Старое имя'");
    }

    private String[] testIsolationLevel(Isolation isolation, UUID leadId) {
        TransactionTemplate tx1 = new TransactionTemplate(transactionManager);
        tx1.setIsolationLevel(isolation.value());

        String[] answer = new String[2];

        tx1.execute(status -> {


            Lead lead = repository.findById(leadId)
                    .orElseThrow(() -> new RuntimeException("Lead not found with id: " + leadId));

            String firstRead = lead.getName();

            //Логируем
            transactionName = tx1.getName();
            log.info(transactionName + isolation.name() + masterTransaction + "- Первый SELECT: Имя лида = " + firstRead);

            // Делаем другую транзакцию с Propagation REQUIRES_NEW
            TransactionTemplate tx2 = new TransactionTemplate(transactionManager);
            tx2.setPropagationBehavior(Propagation.REQUIRES_NEW.value());

            tx2.execute(status1 -> {

                Lead leadToUpdate = repository.findById(leadId)
                        .orElseThrow(() -> new RuntimeException("Lead not found"));
                leadToUpdate.setName("Новое имя!!!");
                repository.save(leadToUpdate);
                repository.flush();

                //Логируем
                transactionName = tx2.getName();
                log.info(transactionName + isolation.name() + otherTransaction + "имя лида изменено на 'Новое имя!!!'");
                return null;
            });

            entityManager.clear();

            Lead leadAfterUpdate = repository.findById(leadId)
                    .orElseThrow(() -> new RuntimeException("Lead not found"));
            String secondRead = leadAfterUpdate.getName();

            transactionName = tx1.getName();
            log.info(isolation.name() + masterTransaction + "- Второй SELECT: Имя лида = " + secondRead);

            answer[0] = firstRead;
            answer[1] = secondRead;

            return null;
        });

        return answer;
    }


}
