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
        // TODO: создать Address
        // TODO: создать Contact с Address
        // TODO: создать Lead с UUID, Contact, company, status
        // TODO: проверить что lead.contact() возвращает правильный Contact
    }

    @Test
    void shouldAccessEmailThroughDelegation_whenLeadCreated() {
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "comp", "NEW");

        assertThat(lead.contact().email()).isEqualTo("mvfid@gmail.com");
        assertThat(lead.contact().address().city()).isEqualTo("Moscow");
        // TODO: создать Lead с полной композицией (Lead → Contact → Address)
        // TODO: получить email через делегацию: lead.contact().email()
        // TODO: проверить что email правильный
        // TODO: получить city через делегацию: lead.contact().address().city()
        // TODO: проверить что city правильный
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
        // TODO: создать два Lead с одинаковым UUID, но разными Contact
        // TODO: решить стратегию: equals по id или по всем полям?
        // TODO: реализовать кастомный equals если нужно (для Record можно переопределить)
    }

    @Test
    void shouldThrowException_whenContactIsNull() {
        assertThatThrownBy(
                () -> {
                    Lead lead = new Lead(UUID.randomUUID(), null, "comp", "NEW");
                }).isInstanceOf(IllegalArgumentException.class);
        // TODO: проверить что создание Lead с contact=null бросает IllegalArgumentException
    }

    @Test
    void shouldThrowException_whenInvalidStatus() {
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact = new Contact("mvfid@gmail.com", "76585659", address);

        assertThatThrownBy(
                () -> {
                    Lead lead = new Lead(UUID.randomUUID(), contact, "comp", "status");
                }).isInstanceOf(IllegalArgumentException.class);
        // TODO: проверить что создание Lead с status="INVALID" бросает IllegalArgumentException
        // TODO: разрешены только: "NEW", "QUALIFIED", "CONVERTED"
    }

    @Test
    void shouldDemonstrateThreeLevelComposition_whenAccessingCity() {
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "comp", "NEW");

        assertThat(contact).isEqualTo(lead.contact());
        assertThat(address).isEqualTo(contact.address());
        assertThat(address.city()).isEqualTo("Moscow");
        // TODO: создать полную композицию Lead → Contact → Address
        // TODO: продемонстрировать трёхуровневую делегацию:
        //       Lead lead = ...
        //       Contact contact = lead.contact()         // уровень 1
        //       Address address = contact.address()      // уровень 2
        //       String city = address.city()             // уровень 3
        //
        //       или сокращённо:
        //       String city = lead.contact().address().city()  // 3 уровня одной строкой
        // TODO: проверить что city правильный
    }
}