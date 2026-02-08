package ru.mentee.power.crm.spring.service;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.spring.repository.JpaLeadRepository;
import ru.mentee.power.crm.spring.utility.PropagationInvoker;
import ru.mentee.power.crm.testHelpClasses.DataGenerator;

@SpringBootTest(classes = ru.mentee.power.crm.spring.Application.class)
class PropagationTest {

    Logger log = LoggerFactory.getLogger(PropagationTest.class);

    @Autowired
    JpaLeadRepository leadRepository;

    @Autowired
    PropagationInvoker invoke;

    Lead lead0;
    Lead lead1;
    Lead lead2;
    Lead lead3;
    Lead lead4;

    @BeforeEach
    void setUp() {
        leadRepository.deleteAll();
        lead0 = DataGenerator.generateRandomLead();
        lead1 = DataGenerator.generateRandomLead();
        lead2 = DataGenerator.generateRandomLead();
        lead3 = DataGenerator.generateRandomLead();
        lead4 = DataGenerator.generateRandomLead();

        leadRepository.save(lead0);
        leadRepository.save(lead1);
        leadRepository.save(lead2);
        leadRepository.save(lead3);
        leadRepository.save(lead4);
    }

    @AfterEach
    void tearDown() {
        leadRepository.deleteAll();
    }

    @Test
    void testPropagation_REQUIRED() {

        List<UUID> ids = leadRepository.findAllIds();

        // REQUIRED - все операции в одной транзакции.
        // При ошибке - все транзакция откатывается.
        assertThatThrownBy(() -> invoke.propagationRequired(ids))
                .isInstanceOf(IllegalArgumentException.class);

        // Смотрим на состояние лидов
        String name0 = leadRepository.findById(ids.get(0)).get().getName();
        log.info("Lead 0 name: " + name0);
        String name1 = leadRepository.findById(ids.get(1)).get().getName();
        log.info("Lead 1 name: " + name1);

        // Никакие изменения не должны остаться (транзакция откачена)
        assertThat(leadRepository.findById(ids.get(0)).get().getName())
                .isNotEqualTo("PROCESSED");
    }

    @Test
    void testPropagation_REQUIRES_NEW() {

        List<UUID> ids = leadRepository.findAllIds();

        // REQUIRES new - каждая операция в отдельной транзакции
        invoke.propagationRequiresNew(ids);

        // 0,1 должны быть успешно обработаны
        // 2 выбросит исключение и не будет обработан
        // 3,4 успешно обработаны
        assertThat(leadRepository.findById(ids.get(0)).get().getName())
                .isEqualTo("PROCESSED");

        assertThat(leadRepository.findById(ids.get(1)).get().getName())
                .isEqualTo("PROCESSED");

        assertThat(leadRepository.findById(ids.get(2)).get().getName())
                .isNotEqualTo("PROCESSED");

        assertThat(leadRepository.findById(ids.get(3)).get().getName())
                .isEqualTo("PROCESSED");
    }

    @Test
    void testPropagation_MANDATORY() {

        List<UUID> ids = leadRepository.findAllIds();

        // Нет транзакции, но MANDATORY требует существующую транзакцию
        assertThatThrownBy(() -> invoke.propagationMandatory(ids))
                .isInstanceOf(org.springframework.transaction.IllegalTransactionStateException.class);

        // Никаких изменений быть не должно
        assertThat(leadRepository.findById(ids.get(0)).get().getName())
                .isNotEqualTo("PROCESSED");
    }
}