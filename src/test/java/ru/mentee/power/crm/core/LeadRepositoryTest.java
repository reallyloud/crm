package ru.mentee.power.crm.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

class LeadRepositoryTest {

    @Test
    @DisplayName("Should automatically deduplicate leads by id")
    void shouldDeduplicateLeadsById() {
        LeadRepository leadRepository = new LeadRepository();
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "comp", "NEW");

        boolean firstAdd = leadRepository.add(lead);
        boolean secondAdd = leadRepository.add(lead);

        assertThat(leadRepository.size()).isEqualTo(1);
        assertThat(secondAdd).isFalse();
    }

    @Test
    @DisplayName("Should allow different leads with different ids")
    void shouldAllowDifferentLeads() {
        LeadRepository leadRepository = new LeadRepository();
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
        Lead lead1 = new Lead(UUID.randomUUID(), contact, "comp", "NEW");
        Lead lead2 = new Lead(UUID.randomUUID(), contact, "comp", "NEW");

        boolean firstAdd = leadRepository.add(lead1);
        boolean secondAdd = leadRepository.add(lead2);

        assertThat(firstAdd).isEqualTo(secondAdd).isTrue();
        assertThat(leadRepository.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should find existing lead through contains")
    void shouldFindExistingLead() {
        LeadRepository leadRepository = new LeadRepository();
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "comp", "NEW");

        leadRepository.add(lead);

        assertThat(leadRepository.contains(lead)).isTrue();
    }

    @Test
    @DisplayName("Should return unmodifiable set from findAll")
    void shouldReturnUnmodifiableSet() {
        LeadRepository leadRepository = new LeadRepository();
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "comp", "NEW");

        leadRepository.add(lead);
        Set<Lead> leadRepositoryCopy = leadRepository.findAll();
        assertThatThrownBy(() -> {
            leadRepositoryCopy.add(lead);
                }).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Should perform contains() faster than ArrayList")
    void shouldPerformFasterThanArrayList() {
        LeadRepository hashSet = new LeadRepository();
        ArrayList<Lead> arrayList= new ArrayList<>();

        LeadRepository leadRepository = new LeadRepository();
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "comp", "NEW");

        for (int i = 0; i < 10000; i++) {
            lead = new Lead(UUID.randomUUID(), contact, "comp", "NEW");
            hashSet.add(lead);
            arrayList.add(lead);
        }

        long hashSetStart = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            hashSet.contains(lead);
        }
        long hashSetTime = System.nanoTime() - hashSetStart;


        long arrayListStart = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            arrayList.contains(lead);
        }
        long arrayListTime = System.nanoTime() - arrayListStart;

        assertThat(hashSetTime < arrayListTime).isTrue();
        // Подсказка: используйте System.nanoTime() для замера времени
        // long start = System.nanoTime();
        // ... операции ...
        // long duration = System.nanoTime() - start;
    }



}