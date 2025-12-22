package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ContactTest {

    @Test
    void shouldCreateContact_whenValidData() {
        Address address = new Address("Moscow","Street","zipka");
        Contact contact = new Contact("mail@gmail.ru","86943067",address);
        assertThat(contact.address()).isEqualTo(address);
        assertThat(address.city()).isEqualTo(contact.address().city());
        // TODO: создать Address
        // TODO: создать Contact с email, phone, address
        // TODO: проверить что contact.address() возвращает правильный Address
        // TODO: проверить делегацию: contact.address().city() возвращает город
    }

    @Test
    void shouldDelegateToAddress_whenAccessingCity() {
        Address address = new Address("Moscow","Street","zipka");
        Contact contact = new Contact("mail@gmail.ru","86943067",address);

        assertThat(contact.address().city()).isEqualTo("Moscow");
        assertThat(contact.address().street()).isEqualTo("Street");

        // TODO: создать Contact с Address
        // TODO: проверить что contact.address().city() работает корректно
        // TODO: проверить что contact.address().street() работает корректно
    }

    @Test
    void shouldThrowException_whenAddressIsNull() {
        assertThatThrownBy(
                () -> {
                    Contact contact = new Contact("mila@mail.com","89694083",null);
                }
        ).isInstanceOf(IllegalArgumentException.class);
        // TODO: проверить что создание Contact с address=null бросает IllegalArgumentException
    }
}
