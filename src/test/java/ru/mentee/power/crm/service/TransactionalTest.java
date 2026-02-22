package ru.mentee.power.crm.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.spring.repository.JpaLeadRepository;
import ru.mentee.power.crm.spring.service.JpaLeadService;
import ru.mentee.power.crm.testHelpClasses.DataGenerator;

@SpringBootTest(classes = ru.mentee.power.crm.spring.Application.class)
@ActiveProfiles("test")
@Transactional
class TransactionalTest {

  private static final Logger log = LoggerFactory.getLogger(TransactionalTest.class);

  @Autowired private JpaLeadService service;

  @Autowired private JpaLeadRepository repository;

  @BeforeEach
  void setUp() {
    repository.deleteAll();

    Lead lead1 = DataGenerator.generateRandomLead();
    Lead lead2 = DataGenerator.generateRandomLead();
    Lead lead3 = DataGenerator.generateRandomLead();

    repository.save(lead1);
    repository.save(lead2);
    repository.save(lead3);
  }

  @Test
  void convertNewToContacted_shouldUpdateMultipleLeads() {
    // When
    int leadsContacted = service.findByStatuses(LeadStatus.CONTACTED).size();
    int leadsNew = service.findByStatuses(LeadStatus.NEW).size();
    int updated = service.convertNewToContacted();

    // Then
    assertThat(updated).isEqualTo(leadsNew);

    // Проверяем что статус изменился
    long contactedCount = repository.countByStatus(LeadStatus.CONTACTED);
    assertThat(contactedCount).isEqualTo(updated + leadsContacted);
  }

  @Test
  void archiveOldLeads() {
    // When
    int contacted = service.findByStatuses(LeadStatus.CONTACTED).size();
    int deleted = service.archiveOldLeads(LeadStatus.CONTACTED);
    List<Lead> leads = repository.findAll();

    // Then
    assertThat(contacted).isEqualTo(deleted);
  }
}
