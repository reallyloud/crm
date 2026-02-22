package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AddressTest {

  @Test
  void shouldCreateAddress_whenValidData() {
    Address address = new Address("San Francisco", "123 Main St", "94105");
    assertThat(address.city()).isEqualTo("San Francisco");
    assertThat(address.zip()).isEqualTo("94105");
    assertThat(address.street()).isEqualTo("123 Main St");
  }

  @Test
  void shouldBeEqual_whenSameData() {
    Address address1 = new Address("San Francisco", "123 Main St", "94105");
    Address address2 = new Address("San Francisco", "123 Main St", "94105");

    assertThat(address1.equals(address2)).isTrue();
    assertThat(address1.hashCode()).isEqualTo(address2.hashCode());
  }

  @Test
  void shouldThrowException_whenCityIsNull() {
    assertThatThrownBy(
            () -> {
              Address address = new Address(null, "123 Main St", "94105");
            })
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldThrowException_whenZipIsBlank() {
    assertThatThrownBy(
            () -> {
              Address address = new Address("San Francisco", "123 Main St", null);
            })
        .isInstanceOf(IllegalArgumentException.class);
  }
}
