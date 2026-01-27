package ru.mentee.power.crm.spring.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.model.LeadStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("application-test")
class JpaLeadRepositoryTest {

    @Autowired
    private JpaLeadRepository repository;

    @Test
    void shouldSaveAndFindLeadById_whenValidData() {
        // Given
        Lead lead = new Lead();
        lead.setName("Олег");
        lead.setEmail("test@example.com");
        lead.setCompany("ACME");
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
        Lead lead = new Lead();
        lead.setName("Олег");
        lead.setEmail("native@test.com");
        lead.setCompany("TechCorp");
        lead.setStatus(LeadStatus.NEW);
        lead.setPhone("+1234567890");
        repository.save(lead);

        // When
        Optional<Lead> found = repository.findByEmailNative("native@test.com");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getCompany()).isEqualTo("TechCorp");
    }

    @Test
    void shouldReturnEmptyOptional_whenEmailNotFound() {
        // When
        Optional<Lead> found = repository.findByEmailNative("nonexistent@test.com");

        assertThat(found).isEmpty();
        // Then
        // TODO: assertThat(found).isEmpty()
    }

    @Test
    void findAllTest() {
        //Given
        Lead lead1 = new Lead();
        lead1.setName("Олег");
        lead1.setEmail("native1@test.com");
        lead1.setCompany("TechCorp1");
        lead1.setStatus(LeadStatus.NEW);
        lead1.setPhone("+1234567890");

        Lead lead2 = new Lead();
        lead2.setName("Максим");
        lead2.setEmail("native2@test.com");
        lead2.setCompany("TechCorp2");
        lead2.setStatus(LeadStatus.NEW);
        lead2.setPhone("+1234567891");

        Lead lead3 = new Lead();
        lead3.setName("Гена");
        lead3.setEmail("native3@test.com");
        lead3.setCompany("TechCorp3");
        lead3.setStatus(LeadStatus.NEW);
        lead3.setPhone("+1234567892");

        //When
        repository.save(lead1);
        repository.save(lead2);
        repository.save(lead3);

        List<Lead> found = repository.findAll();
        assertThat(found).hasSize(3);

    }
}
