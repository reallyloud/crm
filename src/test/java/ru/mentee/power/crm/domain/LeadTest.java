package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class LeadTest {

    @Test
    void shouldCreateLead_whenValidData() {
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "comp", "NEW");
        assertThat(lead.contact()).isEqualTo(contact);
    }

    @Test
    void shouldAccessEmailThroughDelegation_whenLeadCreated() {
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "comp", "NEW");

        assertThat(lead.contact().email()).isEqualTo("mvfid@gmail.com");
        assertThat(lead.contact().address().city()).isEqualTo("Moscow");
    }

    @Test
    void shouldBeEqual_whenSameIdButDifferentContact() {
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact1 = new Contact("gfdhj@gmail.com", "57455659", address);
        Contact contact2 = new Contact("mvfid@gmail.com", "76585659", address);
        UUID randomUUID = UUID.randomUUID();

        Lead lead1 = new Lead(randomUUID, contact1, "comp", "NEW");
        Lead lead2 = new Lead(randomUUID, contact2, "comp", "NEW");

        assertThat(lead1).isEqualTo(lead2);
    }

    @Test
    void shouldThrowException_whenContactIsNull() {
        assertThatThrownBy(
                () -> {
                    Lead lead = new Lead(UUID.randomUUID(), null, "comp", "NEW");
                }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowException_whenInvalidStatus() {
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact = new Contact("mvfid@gmail.com", "76585659", address);

        assertThatThrownBy(
                () -> {
                    Lead lead = new Lead(UUID.randomUUID(), contact, "comp", "status");
                }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldDemonstrateThreeLevelComposition_whenAccessingCity() {
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "comp", "NEW");

        assertThat(contact).isEqualTo(lead.contact());
        assertThat(address).isEqualTo(contact.address());
        assertThat(address.city()).isEqualTo("Moscow");
    }
}