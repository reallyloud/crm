package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ContactTest {

    @Test
    void shouldCreateContact_whenValidData() {
        Contact contact = new Contact("John","Doe","john@example.com");
        assertThat(contact.firstName()).isEqualTo("John");
        assertThat(contact.lastName()).isEqualTo("Doe");
        assertThat(contact.email()).isEqualTo("john@example.com");

        // TODO: создать Contact с firstName="John", lastName="Doe", email="john@example.com"
        // TODO: проверить что все геттеры возвращают правильные значения
    }

    @Test
    void shouldBeEqual_whenSameData() {
        Contact contact1 = new Contact("John","Doe","john@example.com");
        Contact contact2 = new Contact("John","Doe","john@example.com");
        assertThat(contact2.equals(contact1)).isTrue();
        assertThat(contact1.equals(contact2)).isTrue();
        assertThat(contact1.hashCode() == contact2.hashCode()).isTrue();
        // TODO: создать два Contact с одинаковыми данными
        // TODO: проверить что они равны через equals
        // TODO: проверить что hashCode одинаковый
    }

    @Test
    void shouldNotBeEqual_whenDifferentData() {
        Contact contact1 = new Contact("John","Doe","john@example.com");
        Contact contact2 = new Contact("Donald","Trump","trump@example.com");
        assertThat(contact2.equals(contact1)).isFalse();
        assertThat(contact1.equals(contact2)).isFalse();

        // TODO: создать два Contact с разными данными
        // TODO: проверить что они НЕ равны
    }
}
