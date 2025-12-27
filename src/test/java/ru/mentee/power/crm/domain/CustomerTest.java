package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;

class CustomerTest {

    @Test
    void shouldReuseContact_whenCreatingCustomer() {
        Address address = new Address("Moscow","Street","zipka");
        Address billingAddress = new Address("Sochi","Putin","ziip");
        Contact contact = new Contact("mail@gmail.ru","86943067",address);
        Customer customer = new Customer(UUID.randomUUID(),contact,billingAddress,"BRONZE");

        assertThat(customer.contact().address()).isNotEqualTo(customer.billingAddress());
        // TODO: создать Contact и два разных Address (один для contact, один для billing)
        // TODO: создать Customer с contact и billingAddress
        // TODO: проверить что customer.contact().address() != customer.billingAddress()
        // TODO: демонстрация: один Customer, два Address через композицию
    }

    @Test
    void shouldDemonstrateContactReuse_acrossLeadAndCustomer() {
        Address address = new Address("Moscow","Street","zipka");
        Contact contact = new Contact("mail@gmail.ru","86943067",address);

        Customer customer = new Customer(UUID.randomUUID(),contact,address,"BRONZE");
        Lead lead = new Lead(UUID.randomUUID(),contact,"comp","NEW");

        assertThat(customer.contact()).isEqualTo(lead.contact());

    }
}
