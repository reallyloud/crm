package ru.mentee.power.crm.infrastructure;

import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class InMemoryLeadRepositoryTest {

    @Test
    void addUnique() {
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "comp", "NEW");
        InMemoryLeadRepository repository = new InMemoryLeadRepository();

        repository.add(lead);

        assertThat(repository.findAll()).contains(lead);
    }

    @Test
    void addDuplicateRejected() {
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
        UUID randomID = UUID.randomUUID();
        Lead lead1 = new Lead(randomID, contact, "comp", "NEW");
        Lead lead2 = new Lead(randomID, contact, "comp", "NEW");
        InMemoryLeadRepository repository = new InMemoryLeadRepository();

        repository.add(lead1);
        repository.add(lead2);

        assertThat(repository.findAll().size()).isEqualTo(1);
    }

    @Test
    void findByIdFound() {
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
        UUID randomID = UUID.randomUUID();
        Lead lead = new Lead(randomID, contact, "comp", "NEW");
        InMemoryLeadRepository repository = new InMemoryLeadRepository();

        repository.add(lead);

        assertThat(repository.findById(randomID)).isEqualTo(Optional.of(lead));


    }

    @Test
    void findByIdNotFound() {
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "comp", "NEW");
        InMemoryLeadRepository repository = new InMemoryLeadRepository();

        repository.add(lead);

        assertThat(repository.findById(UUID.randomUUID())).isEqualTo(Optional.empty());

    }

    @Test
    void removeExisting() {
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
        UUID randomID1 = UUID.randomUUID();
        UUID randomID2 = UUID.randomUUID();
        UUID randomID3 = UUID.randomUUID();
        UUID randomID4 = UUID.randomUUID();
        Lead lead1 = new Lead(randomID1, contact, "comp", "NEW");
        Lead lead2 = new Lead(randomID2, contact, "comp", "NEW");
        Lead lead3 = new Lead(randomID3, contact, "comp", "NEW");
        Lead lead4 = new Lead(randomID4, contact, "comp", "NEW");
        InMemoryLeadRepository repository = new InMemoryLeadRepository();

        repository.add(lead1);
        repository.add(lead2);
        repository.add(lead3);
        repository.add(lead4);

        assertThat(repository.findAll()).contains(lead4);
        assertThat(repository.findAll().size()).isEqualTo(4);

        repository.remove(randomID4);

        assertThat(repository.findAll().size()).isEqualTo(3);
    }

    @Test
    void findAllDefensiveCopy() {
        Address address = new Address("Moscow", "Street", "zipka");
        Contact contact = new Contact("mvfid@gmail.com", "76585659", address);
        UUID randomID = UUID.randomUUID();
        Lead lead = new Lead(randomID, contact, "comp", "NEW");
        InMemoryLeadRepository repository = new InMemoryLeadRepository();

        repository.add(lead);
        List<Lead> list = repository.findAll();
        list.removeFirst();

        assertThat(list.size()).isEqualTo(0);
        assertThat(repository.findById(randomID)).isEqualTo(Optional.of(lead));
    }


}