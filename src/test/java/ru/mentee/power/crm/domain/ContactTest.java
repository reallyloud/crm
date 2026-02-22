package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ContactTest {

  @Test
  void shouldCreateContact_whenValidData() {
    Address address = new Address("Moscow", "Street", "zipka");
    Contact contact = new Contact("mail@gmail.ru", "86943067", address);
    assertThat(contact.address()).isEqualTo(address);
    assertThat(address.city()).isEqualTo(contact.address().city());
  }

  @Test
  void shouldDelegateToAddress_whenAccessingCity() {
    Address address = new Address("Moscow", "Street", "zipka");
    Contact contact = new Contact("mail@gmail.ru", "86943067", address);

    assertThat(contact.address().city()).isEqualTo("Moscow");
    assertThat(contact.address().street()).isEqualTo("Street");
  }

  @Test
  void shouldThrowException_whenAddressIsNull() {
    assertThatThrownBy(
            () -> {
              Contact contact = new Contact("mila@mail.com", "89694083", null);
            })
        .isInstanceOf(IllegalArgumentException.class);
  }
}
