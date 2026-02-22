package ru.mentee.power.crm.spring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.entity.Company;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.spring.repository.JpaLeadRepository;
import ru.mentee.power.crm.testHelpClasses.DataGenerator;

@ActiveProfiles("test")
@SpringBootTest(classes = ru.mentee.power.crm.spring.Application.class)
@Transactional
class JpaLeadServiceIntegrationTest {

  @Autowired private JpaLeadService leadService;

  @Autowired private JpaLeadRepository leadRepository;

  private Lead savedLead;

  @BeforeEach
  void setUp() {
    leadRepository.deleteAll();

    Lead lead = DataGenerator.generateRandomLead();
    lead.setStatus(LeadStatus.NEW);
    savedLead = leadRepository.save(lead);
  }

  @Test
  void findAll_shouldReturnAllLeads() {
    Lead lead2 = DataGenerator.generateRandomLead();
    lead2.setStatus(LeadStatus.CONTACTED);
    leadRepository.save(lead2);

    List<Lead> leads = leadService.findAll();

    assertThat(leads).hasSize(2);
  }

  @Test
  void findById_shouldReturnLeadWhenExists() {
    Optional<Lead> found = leadService.findById(savedLead.getId());

    assertThat(found).isPresent();
    assertThat(found.get().getEmail()).isEqualTo(savedLead.getEmail());
  }

  @Test
  void findById_shouldReturnEmptyWhenNotExists() {
    Optional<Lead> found = leadService.findById(UUID.randomUUID());

    assertThat(found).isEmpty();
  }

  @Test
  void save_shouldPersistNewLead() {
    Company company = new Company();
    company.setName("NewCompany");
    company.setIndustry("IT");

    Lead newLead = new Lead("Новый", "new@email.com", company, LeadStatus.NEW);
    newLead.setPhone("123456789");

    Lead saved = leadService.save(newLead);

    assertThat(saved.getId()).isNotNull();
    assertThat(leadRepository.findById(saved.getId())).isPresent();
  }

  @Test
  void delete_shouldRemoveLead() {
    UUID id = savedLead.getId();
    assertThat(leadRepository.existsById(id)).isTrue();

    leadService.delete(id);

    assertThat(leadRepository.existsById(id)).isFalse();
  }

  @Test
  void delete_shouldThrowWhenNotFound() {
    UUID nonExistentId = UUID.randomUUID();

    assertThatThrownBy(() -> leadService.delete(nonExistentId))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  void update_shouldModifyExistingLead() {
    UUID id = savedLead.getId();

    Lead updated =
        leadService.update(
            id,
            "Обновленный",
            "updated@email.com",
            "999999",
            "НоваяКомпания",
            LeadStatus.CONTACTED);

    assertThat(updated.getName()).isEqualTo("Обновленный");
    assertThat(updated.getEmail()).isEqualTo("updated@email.com");
    assertThat(updated.getPhone()).isEqualTo("999999");
    assertThat(updated.getStatus()).isEqualTo(LeadStatus.CONTACTED);
    assertThat(updated.getCompany().getName()).isEqualTo("НоваяКомпания");
  }

  @Test
  void update_shouldThrowWhenNotFound() {
    UUID nonExistentId = UUID.randomUUID();

    assertThatThrownBy(
            () ->
                leadService.update(
                    nonExistentId, "name", "email@t.com", "123", "comp", LeadStatus.NEW))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  void findLeads_shouldFilterBySearch() {
    Lead lead2 = DataGenerator.generateRandomLead();
    lead2.setEmail("unique-search-test@example.com");
    lead2.setName("UniqueSearchName");
    leadRepository.save(lead2);

    List<Lead> results = leadService.findLeads("unique-search-test", null);

    assertThat(results).hasSize(1);
    assertThat(results.getFirst().getEmail()).isEqualTo("unique-search-test@example.com");
  }

  @Test
  void findLeads_shouldFilterByStatus() {
    Lead lead2 = DataGenerator.generateRandomLead();
    lead2.setStatus(LeadStatus.CONTACTED);
    leadRepository.save(lead2);

    Lead lead3 = DataGenerator.generateRandomLead();
    lead3.setStatus(LeadStatus.CONTACTED);
    leadRepository.save(lead3);

    List<Lead> results = leadService.findLeads(null, "CONTACTED");

    assertThat(results).hasSize(2);
    assertThat(results).allMatch(l -> l.getStatus() == LeadStatus.CONTACTED);
  }

  @Test
  void findLeads_shouldFilterBySearchAndStatus() {
    Lead lead2 = DataGenerator.generateRandomLead();
    lead2.setEmail("combo-filter@example.com");
    lead2.setStatus(LeadStatus.QUALIFIED);
    leadRepository.save(lead2);

    Lead lead3 = DataGenerator.generateRandomLead();
    lead3.setEmail("combo-filter-other@example.com");
    lead3.setStatus(LeadStatus.NEW);
    leadRepository.save(lead3);

    List<Lead> results = leadService.findLeads("combo-filter", "QUALIFIED");

    assertThat(results).hasSize(1);
    assertThat(results.getFirst().getEmail()).isEqualTo("combo-filter@example.com");
  }

  @Test
  void findLeads_shouldReturnAllWhenNoFilters() {
    Lead lead2 = DataGenerator.generateRandomLead();
    leadRepository.save(lead2);

    List<Lead> results = leadService.findLeads(null, null);

    assertThat(results).hasSize(2);
  }

  @Test
  void findLeads_shouldReturnEmptyWhenNoMatch() {
    List<Lead> results = leadService.findLeads("nonexistent-email-xyz", null);

    assertThat(results).isEmpty();
  }
}
