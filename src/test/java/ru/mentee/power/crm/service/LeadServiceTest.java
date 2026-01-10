package ru.mentee.power.crm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.spring.repository.InMemoryLeadRepository;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.service.LeadService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class LeadServiceTest {

    private LeadService service;
    private InMemoryLeadRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLeadRepository();
        service = new LeadService(repository);
    }

    @Test
    void shouldCreateLead_whenEmailIsUnique() {
        // Given
        String email = "test@example.com";
        String company = "Test Company";
        LeadStatus status = LeadStatus.NEW;

        // When
        Lead result = service.addLead(email, company, status);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.contact().email()).isEqualTo(email);
        assertThat(result.company()).isEqualTo(company);
        assertThat(result.status()).isEqualTo(status);
        assertThat(result.id()).isNotNull();
    }

    @Test
    void shouldThrowException_whenEmailAlreadyExists() {
        // Given
        String email = "duplicate@example.com";
        service.addLead(email, "First Company", LeadStatus.NEW);

        // When/Then
        assertThatThrownBy(() ->
                service.addLead(email, "Second Company", LeadStatus.NEW)
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Лид с таким Email уже существует!");
    }

    @Test
    void shouldFindAllLeads() {
        // Given
        service.addLead("one@example.com", "Company 1", LeadStatus.NEW);
        service.addLead("two@example.com", "Company 2", LeadStatus.CONTACTED);

        // When
        List<Lead> result = service.findAll();

        // Then
        assertThat(result).hasSize(2);
    }

    @Test
    void shouldFindLeadById() {
        // Given
        Lead created = service.addLead("find@example.com", "Company", LeadStatus.NEW);

        // When
        Optional<Lead> result = service.findById(created.id());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().contact().email()).isEqualTo("find@example.com");
    }

    @Test
    void shouldFindLeadByEmail() {
        // Given
        service.addLead("search@example.com", "Company", LeadStatus.NEW);

        // When
        Optional<Lead> result = service.findByEmail("search@example.com");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().company()).isEqualTo("Company");
    }

    @Test
    void shouldReturnEmpty_whenLeadNotFound() {
        // Given/When
        Optional<Lead> result = service.findByEmail("nonexistent@example.com");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnOnlyNewLeads_whenFindByStatusNew() {
        //Given
        LeadService leadService = new LeadService(new InMemoryLeadRepository());
        leadService.addLead("maximalen1999@gmail.com", "yandex", LeadStatus.NEW); // Положили туда лидов
        leadService.addLead("oleg@gmail.com", "google", LeadStatus.NEW);
        leadService.addLead("gennadiy@gmail.com", "lada", LeadStatus.NEW);
        leadService.addLead("auto@gmail.com", "granta", LeadStatus.CONTACTED);
        leadService.addLead("omaigad@gmail.com", "pepa", LeadStatus.CONTACTED);
        leadService.addLead("шнейнепепа@gmail.com", "watawa", LeadStatus.CONTACTED);
        leadService.addLead("OOO@gaddem.com", "gangster", LeadStatus.CONTACTED);
        leadService.addLead("drake@gmail.com", "toosieslide", LeadStatus.CONTACTED);
        leadService.addLead("bigbabytape@gaddem.com", "dragonborn", LeadStatus.QUALIFIED);
        leadService.addLead("super@gmail.com", "man", LeadStatus.QUALIFIED);

        //When
        List<Lead> result = leadService.findByStatus(LeadStatus.NEW);

        //Then
        assertThat(result).hasSize(3);
        assertThat(result).allMatch(lead -> lead.status().equals(LeadStatus.NEW));
    }

    @Test
    void shouldReturnEmptyList_whenNoLeadsWithStatus() {
        //Given
        LeadService leadService = new LeadService(new InMemoryLeadRepository());
        leadService.addLead("maximalen1999@gmail.com", "yandex", LeadStatus.NEW); // Положили туда лидов
        leadService.addLead("oleg@gmail.com", "google", LeadStatus.NEW);
        leadService.addLead("gennadiy@gmail.com", "lada", LeadStatus.NEW);
        leadService.addLead("auto@gmail.com", "granta", LeadStatus.CONTACTED);
        leadService.addLead("omaigad@gmail.com", "pepa", LeadStatus.CONTACTED);
        leadService.addLead("шнейнепепа@gmail.com", "watawa", LeadStatus.CONTACTED);
        leadService.addLead("OOO@gaddem.com", "gangster", LeadStatus.CONTACTED);
        leadService.addLead("drake@gmail.com", "toosieslide", LeadStatus.CONTACTED);

        //When
        List<Lead> result = leadService.findByStatus(LeadStatus.QUALIFIED);

        //Then
        assertThat(result).hasSize(0);
    }

    @Test
    void shouldReturnOnlyNewLeads_whenFindByStatusContacted() {
        //Given
        LeadService leadService = new LeadService(new InMemoryLeadRepository());
        leadService.addLead("maximalen1999@gmail.com", "yandex", LeadStatus.NEW); // Положили туда лидов
        leadService.addLead("oleg@gmail.com", "google", LeadStatus.NEW);
        leadService.addLead("gennadiy@gmail.com", "lada", LeadStatus.NEW);
        leadService.addLead("auto@gmail.com", "granta", LeadStatus.CONTACTED);
        leadService.addLead("omaigad@gmail.com", "pepa", LeadStatus.CONTACTED);
        leadService.addLead("шнейнепепа@gmail.com", "watawa", LeadStatus.CONTACTED);
        leadService.addLead("OOO@gaddem.com", "gangster", LeadStatus.CONTACTED);
        leadService.addLead("drake@gmail.com", "toosieslide", LeadStatus.CONTACTED);
        leadService.addLead("bigbabytape@gaddem.com", "dragonborn", LeadStatus.QUALIFIED);
        leadService.addLead("super@gmail.com", "man", LeadStatus.QUALIFIED);

        //When
        List<Lead> result = leadService.findByStatus(LeadStatus.CONTACTED);

        //Then
        assertThat(result).hasSize(5);
        assertThat(result).allMatch(lead -> lead.status().equals(LeadStatus.CONTACTED));
    }

    @Test
    void shouldReturnOnlyNewLeads_whenFindByStatusQualified() {
        //Given
        LeadService leadService = new LeadService(new InMemoryLeadRepository());
        leadService.addLead("maximalen1999@gmail.com", "yandex", LeadStatus.NEW); // Положили туда лидов
        leadService.addLead("oleg@gmail.com", "google", LeadStatus.NEW);
        leadService.addLead("gennadiy@gmail.com", "lada", LeadStatus.NEW);
        leadService.addLead("auto@gmail.com", "granta", LeadStatus.CONTACTED);
        leadService.addLead("omaigad@gmail.com", "pepa", LeadStatus.CONTACTED);
        leadService.addLead("шнейнепепа@gmail.com", "watawa", LeadStatus.CONTACTED);
        leadService.addLead("OOO@gaddem.com", "gangster", LeadStatus.CONTACTED);
        leadService.addLead("drake@gmail.com", "toosieslide", LeadStatus.CONTACTED);
        leadService.addLead("bigbabytape@gaddem.com", "dragonborn", LeadStatus.QUALIFIED);
        leadService.addLead("super@gmail.com", "man", LeadStatus.QUALIFIED);

        //When
        List<Lead> result = leadService.findByStatus(LeadStatus.QUALIFIED);

        //Then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(lead -> lead.status().equals(LeadStatus.QUALIFIED));
    }
}