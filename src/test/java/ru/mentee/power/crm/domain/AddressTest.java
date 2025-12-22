package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class AddressTest {

    @Test
    void shouldCreateAddress_whenValidData() {
        Address address = new Address("San Francisco","123 Main St", "94105");
        assertThat(address.city()).isEqualTo("San Francisco");
        assertThat(address.zip()).isEqualTo("94105");
        assertThat(address.street()).isEqualTo("123 Main St");
        // TODO: создать Address с city="San Francisco", street="123 Main St", zip="94105"
        // TODO: проверить что все геттеры возвращают правильные значения через assertThat
    }

    @Test
    void shouldBeEqual_whenSameData() {
        Address address1 = new Address("San Francisco","123 Main St", "94105");
        Address address2 = new Address("San Francisco","123 Main St", "94105");

        assertThat(address1.equals(address2)).isTrue();
        assertThat(address1.hashCode()).isEqualTo(address2.hashCode());

        // TODO: создать два Address с одинаковыми данными
        // TODO: проверить что они равны через equals
        // TODO: проверить что hashCode одинаковый
    }

    @Test
    void shouldThrowException_whenCityIsNull() {
        // TODO: проверить что создание Address с city=null бросает IllegalArgumentException
        // TODO: использовать assertThatThrownBy
        assertThatThrownBy(
                () -> {
                    Address address = new Address(null,"123 Main St", "94105");
                }
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowException_whenZipIsBlank() {
        assertThatThrownBy(
                () -> {
                    Address address = new Address("San Francisco","123 Main St", null);
                }
        ).isInstanceOf(IllegalArgumentException.class);
        // TODO: проверить что создание Address с zip="" бросает IllegalArgumentException
    }
}
