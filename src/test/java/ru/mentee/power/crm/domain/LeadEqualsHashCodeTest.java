package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class LeadEqualsHashCodeTest {

    @Test
    void shouldBeReflexive_whenEqualsCalledOnSameObject() {
        // Given
        Address address = new Address("Moscow","Street","zipka");
        Contact contact = new Contact("mail@gmd.com","695047654",address);
        Lead lead = new Lead(UUID.randomUUID(),contact,"company","NEW");

        // Then: Объект равен сам себе (isEqualTo использует equals() внутри)
        assertThat(lead).isEqualTo(lead);
    }

    @Test
    void shouldBeSymmetric_whenEqualsCalledOnTwoObjects() {
        // Given
        UUID randomUUID = UUID.randomUUID();
        Address address = new Address("Moscow","Street","zipka");
        Contact contact = new Contact("mail@gmd.com","695047654",address);

        Lead firstLead = new Lead(randomUUID, contact, "+7123", "NEW");
        Lead secondLead = new Lead(randomUUID, contact, "+7123", "NEW");

        // Then: Симметричность — порядок сравнения не важен
        assertThat(firstLead).isEqualTo(secondLead);
        assertThat(secondLead).isEqualTo(firstLead);
    }

    @Test
    void shouldBeTransitive_whenEqualsChainOfThreeObjects() {
        // Given
        UUID randomUUID = UUID.randomUUID();
        Address address = new Address("Moscow","Street","zipka");
        Contact contact = new Contact("mail@gmd.com","695047654",address);



        Lead firstLead = new Lead(randomUUID, contact, "+7123", "NEW");
        Lead secondLead = new Lead(randomUUID, contact, "+7123", "NEW");
        Lead thirdLead = new Lead(randomUUID, contact, "+7123", "NEW");

        // Then: Транзитивность — если A=B и B=C, то A=C
        assertThat(firstLead).isEqualTo(secondLead);
        assertThat(secondLead).isEqualTo(thirdLead);
        assertThat(firstLead).isEqualTo(thirdLead);
    }

    @Test
    void shouldBeConsistent_whenEqualsCalledMultipleTimes() {
        // Given
        UUID randomUUID = UUID.randomUUID();
        Address address = new Address("Moscow","Street","zipka");
        Contact contact = new Contact("mail@gmd.com","695047654",address);


        Lead firstLead = new Lead(randomUUID, contact, "+7123", "NEW");
        Lead secondLead = new Lead(randomUUID, contact, "+7123", "NEW");

        // Then: Результат одинаковый при многократных вызовах
        assertThat(firstLead).isEqualTo(secondLead);
        assertThat(firstLead).isEqualTo(secondLead);
        assertThat(firstLead).isEqualTo(secondLead);
    }

    @Test
    void shouldReturnFalse_whenEqualsComparedWithNull() {
        // Given
        UUID randomUUID = UUID.randomUUID();
        Address address = new Address("Moscow","Street","zipka");
        Contact contact = new Contact("mail@gmd.com","695047654",address);

        Lead lead = new Lead(UUID.randomUUID(), contact, "+7123", "NEW");

        // Then: Объект не равен null (isNotEqualTo проверяет equals(null) = false)
        assertThat(lead).isNotEqualTo(null);
    }

    @Test
    void shouldHaveSameHashCode_whenObjectsAreEqual() {
        // Given
        UUID randomUUID = UUID.randomUUID();
        Address address = new Address("Moscow","Street","zipka");
        Contact contact = new Contact("mail@gmd.com","695047654",address);

        Lead firstLead = new Lead(randomUUID, contact, "+7123", "NEW");
        Lead secondLead = new Lead(randomUUID, contact, "+7123", "NEW");

        // Then: Если объекты равны, то hashCode должен быть одинаковым
        assertThat(firstLead).isEqualTo(secondLead);
        assertThat(firstLead.hashCode()).isEqualTo(secondLead.hashCode());
    }

    @Test
    void shouldWorkInHashMap_whenLeadUsedAsKey() {
        // Given
        UUID randomUUID = UUID.randomUUID();
        Address address = new Address("Moscow","Street","zipka");
        Contact contact = new Contact("mail@gmd.com","695047654",address);


        Lead keyLead = new Lead(randomUUID, contact, "+7123", "NEW");
        Lead lookupLead = new Lead(randomUUID, contact, "+7123", "NEW");

        Map<Lead, String> map = new HashMap<>();
        map.put(keyLead, "CONTACTED");

        // When: Получаем значение по другому объекту с тем же id
        String status = map.get(lookupLead);

        // Then: HashMap нашел значение благодаря equals/hashCode
        assertThat(status).isEqualTo("CONTACTED");
    }

    @Test
    void shouldNotBeEqual_whenIdsAreDifferent() {
        // Given

        Address address1 = new Address("Moscow","Street","zipka");
        Contact contact1 = new Contact("mail@gmd.com","695047654",address1);

        Address address2 = new Address("Moscow","Street","zipka");
        Contact contact2 = new Contact("mail@gmd.com","695047654",address2);


        Lead lead1 = new Lead(UUID.randomUUID(), contact1, "+7123", "NEW");
        Lead lead2 = new Lead(UUID.randomUUID(), contact2, "+7123", "NEW");

        // Then: Разные id = разные объекты (isNotEqualTo использует equals() внутри)
        assertThat(lead1).isNotEqualTo(lead2);
    }
}
