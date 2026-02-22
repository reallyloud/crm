package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;

class LeadTest {

  @Test
  void shouldCreateLead_whenValidData() {
    Address address = new Address("Moscow", "Street", "zipka");
    Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
    Lead lead = new Lead(UUID.randomUUID(), contact, "comp", LeadStatus.NEW);
    assertThat(lead.contact()).isEqualTo(contact);
  }

  @Test
  void shouldAccessEmailThroughDelegation_whenLeadCreated() {
    Address address = new Address("Moscow", "Street", "zipka");
    Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
    Lead lead = new Lead(UUID.randomUUID(), contact, "comp", LeadStatus.NEW);

    assertThat(lead.contact().email()).isEqualTo("mvfid@gmail.com");
    assertThat(lead.contact().address().city()).isEqualTo("Moscow");
  }

  @Test
  void shouldBeEqual_whenSameIdButDifferentContact() {
    Address address = new Address("Moscow", "Street", "zipka");
    Contact contact1 = new Contact("gfdhj@gmail.com", "57455659", address);
    Contact contact2 = new Contact("mvfid@gmail.com", "76585659", address);
    UUID randomUUID = UUID.randomUUID();

    Lead lead1 = new Lead(randomUUID, contact1, "comp", LeadStatus.NEW);
    Lead lead2 = new Lead(randomUUID, contact2, "comp", LeadStatus.NEW);

    assertThat(lead1).isEqualTo(lead2);
  }

  @Test
  void shouldDemonstrateThreeLevelComposition_whenAccessingCity() {
    Address address = new Address("Moscow", "Street", "zipka");
    Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
    Lead lead = new Lead(UUID.randomUUID(), contact, "comp", LeadStatus.NEW);

    assertThat(contact).isEqualTo(lead.contact());
    assertThat(address).isEqualTo(contact.address());
    assertThat(address.city()).isEqualTo("Moscow");
  }
}
