package ru.mentee.power.crm.spring.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.entity.Company;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.testHelpClasses.DataGenerator;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class JpaLeadRepositoryTest {

    @Autowired
    private JpaLeadRepository repository;

    private Lead lead1;
    private Lead lead2;

    @BeforeEach
    void setUp() {
        // Подготовка тестовых данных
        lead1 = new Lead();
        lead1.setName("john");
        lead1.setEmail("john@example.com");
        lead1.setCompany(DataGenerator.generateRandomCompany());
        lead1.setStatus(LeadStatus.NEW);
        lead1.setCreatedAt(LocalDateTime.now().minusDays(5));
        lead1.setPhone("869548793");
        repository.save(lead1);

        lead2 = new Lead();
        lead2.setName("jane");
        lead2.setEmail("jane@example.com");
        lead2.setCompany(DataGenerator.generateRandomCompany());
        lead2.setStatus(LeadStatus.CONTACTED);
        lead2.setPhone("8695493654");
        lead2.setCreatedAt(LocalDateTime.now().minusDays(2));

        repository.save(lead2);
    }

    @Test
    void shouldSaveAndFindLeadById_whenValidData() {
        // Given
        Lead lead = new Lead();
        lead.setName("Олег");
        lead.setEmail("test@example.com");
        lead.setCompany(DataGenerator.generateRandomCompany());
        lead.setStatus(LeadStatus.NEW);
        lead.setPhone("+1234567890");

        // When
        Lead saved = repository.save(lead);
        Optional<Lead> found = repository.findById(saved.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void shouldFindByEmailNative_whenLeadExists() {
        // Given
        Company company = DataGenerator.generateRandomCompany();
        Lead lead = new Lead();
        lead.setName("Олег");
        lead.setEmail("native@test.com");
        lead.setCompany(company);
        lead.setStatus(LeadStatus.NEW);
        lead.setPhone("+1234567890");
        repository.save(lead);

        // When
        Optional<Lead> found = repository.findByEmail("native@test.com");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getCompany()).isEqualTo(company);
    }

    @Test
    void shouldReturnEmptyOptional_whenEmailNotFound() {
        // When
        Optional<Lead> found = repository.findByEmail("nonexistent@test.com");

        assertThat(found).isEmpty();
        // Then
        // TODO: assertThat(found).isEmpty()
    }

    @Test
    void findAllTest() {
        //Given
        lead1.setName("Олег");
        lead1.setEmail("native1@test.com");
        lead1.setCompany(DataGenerator.generateRandomCompany());
        lead1.setStatus(LeadStatus.NEW);
        lead1.setPhone("+1234567890");

        lead2.setName("Максим");
        lead2.setEmail("native2@test.com");
        lead2.setCompany(DataGenerator.generateRandomCompany());
        lead2.setStatus(LeadStatus.NEW);
        lead2.setPhone("+1234567891");

        Lead lead3 = new Lead();
        lead3.setName("Гена");
        lead3.setEmail("native3@test.com");
        lead3.setCompany(DataGenerator.generateRandomCompany());
        lead3.setStatus(LeadStatus.NEW);
        lead3.setPhone("+1234567892");

        //When
        repository.save(lead1);
        repository.save(lead2);
        repository.save(lead3);

        List<Lead> found = repository.findAll();
        assertThat(found).hasSize(3);

    }

    @Test
    void findByEmail_shouldReturnLead_whenExists() {
        // When
        Optional<Lead> found = repository.findByEmail("john@example.com");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getPhone()).isEqualTo("869548793");
    }

    @Test
    void findByStatus_shouldReturnFilteredLeads() {
        // When
        List<Lead> newLeads = repository.findByStatus(LeadStatus.NEW);

        // Then
        assertThat(newLeads).hasSize(1);
        assertThat(newLeads.get(0).getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void findByStatusIn_shouldReturnLeadsWithMultipleStatuses() {
        // Given
        List<LeadStatus> statuses = List.of(LeadStatus.NEW, LeadStatus.CONTACTED);

        // When
        List<Lead> found = repository.findByStatusIn(statuses);

        // Then
        assertThat(found).hasSize(2);
    }

    @Test
    void findAll_withPageable_shouldReturnPage() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 1);

        // When
        Page<Lead> page = repository.findAll(pageRequest);

        // Then
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getNumber()).isEqualTo(0); // текущая страница
    }

    @Test
    void countByStatus() {
        //When
        long result = repository.countByStatus(LeadStatus.valueOf("NEW"));

        //Then
        assertThat(result).isEqualTo(1);
    }


    @Test
    void existsByEmail() {
        //When
        boolean result = repository.existsByEmail("john@example.com");

        //Then
        assertThat(result).isTrue();
    }

    @Test
    void findByStatusAndCompany() {
        Lead lead = DataGenerator.generateRandomLead();
        lead.setStatus(LeadStatus.NEW);
        String companyName = lead.getCompany().getName();
        repository.save(lead);

        //When
        List<Lead> result = repository.findByStatusAndCompanyName(LeadStatus.NEW,companyName);

        //Then
        assertThat(result).hasSize(1);
    }

}
