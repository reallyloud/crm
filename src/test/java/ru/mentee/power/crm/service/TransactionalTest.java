package ru.mentee.power.crm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.repository.JpaLeadRepository;
import ru.mentee.power.crm.spring.service.JpaLeadService;

import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("application-test")
@SpringBootTest(classes = ru.mentee.power.crm.spring.Application.class)
@Transactional
class TransactionalTest {

    @Autowired
    private JpaLeadService service;

    @Autowired
    private JpaLeadRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        // Создаём 3 NEW лида
        for (int i = 1; i <= 3; i++) {
            Lead lead = new Lead();
            lead.setName("lead" + i);
            lead.setEmail("lead" + i + "@example.com");
            lead.setCompany("Company " + i);
            lead.setPhone("785694789" + i);
            lead.setStatus(LeadStatus.NEW);
            repository.save(lead);
        }
    }

    @Test
    void convertNewToContacted_shouldUpdateMultipleLeads() {
        // When
        int updated = service.convertNewToContacted();

        // Then
        assertThat(updated).isEqualTo(3);

        // Проверяем что статус изменился
        long contactedCount = repository.countByStatus(LeadStatus.CONTACTED);
        assertThat(contactedCount).isEqualTo(3);

        long newCount = repository.countByStatus(LeadStatus.NEW);
        assertThat(newCount).isEqualTo(0);
    }

    @Test
    void archiveOldLeads() {
        //When
        int deleted = service.archiveOldLeads(LeadStatus.NEW);
        List<Lead> leads = repository.findAll();


        //Then
        assertThat(deleted).isEqualTo(3);
        assertThat(leads).isEmpty();
    }

    // TODO: Студент добавляет тест для метода archiveOldLeads
}