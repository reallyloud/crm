package ru.mentee.power.crm.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class LeadRepositoryTest {
    private LeadRepository repository;

    @BeforeEach
    void setUp() {
        repository = new LeadRepository();
    }

    @Test
    void shouldSaveAndFindLeadById_whenLeadSaved() {
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(id, contact, "comp", "NEW");
        repository.save(lead);
        assertThat(repository.findById(id)).isNotNull();
    }

    @Test
    void shouldReturnNull_whenLeadNotFound() {
        assertThat(repository.findById(UUID.randomUUID())).isNull();
    }

    @Test
    void shouldReturnAllLeads_whenMultipleLeadsSaved() {
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();
        Lead lead1 = new Lead(id1, contact, "comp", "NEW");
        Lead lead2 = new Lead(id2, contact, "comp", "NEW");
        Lead lead3 = new Lead(id3, contact, "comp", "NEW");
        repository.save(lead1);
        repository.save(lead2);
        repository.save(lead3);
        List<Lead> copy = repository.findAll();

        assertThat(copy).hasSize(3);
    }

    @Test
    void shouldDeleteLead_whenLeadExists() {
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(id, contact, "comp", "NEW");
        repository.save(lead);
        repository.delete(id);
        List<Lead> copy = repository.findAll();

        assertThat(repository.findById(id)).isNull();
        assertThat(copy).hasSize(0);
    }

    @Test
    void shouldOverwriteLead_whenSaveWithSameId() {
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        Lead lead1 = new Lead(id1, contact, "comp", "NEW");
        Lead lead2 = new Lead(id1, contact, "company2", "NEW");

        repository.save(lead1);
        repository.save(lead2);

        assertThat(repository.findById(id1)).isEqualTo(lead2);

    }

    @Test
    void shouldFindFasterWithMap_thanWithListFilter() {
        // Given: Создать 1000 лидов
        List<Lead> leadList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            UUID id = UUID.randomUUID();
            Contact contact = new Contact(
                    "email" + i + "@test.com",
                    "+7" + i,
                    new Address("City" + i, "Street" + i, "ZIP" + i)
            );
            Lead lead = new Lead(id, contact, "Company" + i, "NEW");
            repository.save(lead);
            leadList.add(lead);
        }

        UUID targetId = UUID.randomUUID();  // Средний элемент

        // When: Поиск через Map
        long mapStart = System.nanoTime();
        Lead foundInMap = repository.findById(targetId);
        long mapDuration = System.nanoTime() - mapStart;

        // When: Поиск через List.stream().filter()
        long listStart = System.nanoTime();
        Lead foundInList = leadList.stream()
                .filter(lead -> lead.id().equals(targetId))
                .findFirst()
                .orElse(null);
        long listDuration = System.nanoTime() - listStart;

        // Then: Map должен быть минимум в 10 раз быстрее
        assertThat(foundInMap).isEqualTo(foundInList);
        assertThat(listDuration).isGreaterThan(mapDuration * 10);

        System.out.println("Map поиск: " + mapDuration + " ns");
        System.out.println("List поиск: " + listDuration + " ns");
        System.out.println("Ускорение: " + (listDuration / mapDuration) + "x");
    }

    @Test
    void shouldSaveBothLeads_evenWithSameEmailAndPhone_becauseRepositoryDoesNotCheckBusinessRules() {
        // Given: два лида с разными UUID но одинаковыми контактами
        Contact sharedContact = new Contact("ivan@mail.ru", "+79001234567",
                new Address("Moscow", "Tverskaya 1", "101000"));
        Lead originalLead = new Lead(UUID.randomUUID(), sharedContact, "Acme Corp", "NEW");
        Lead duplicateLead = new Lead(UUID.randomUUID(), sharedContact, "TechCorp", "QUALIFIED");

        // When: сохраняем оба
        repository.save(originalLead);
        repository.save(duplicateLead);

        // Then: Repository сохранил оба (это технически правильно!)
        assertThat(repository.size()).isEqualTo(2);

        // But: Бизнес недоволен — в CRM два контакта на одного человека
        // Решение: Service Layer в Sprint 5 будет проверять бизнес-правила
        // перед вызовом repository.save()
    }



}
