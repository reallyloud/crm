package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class LeadTest {
    @Test
    void shouldReturnId_whenGetIdCalled() {
        // Given
        UUID randomUUID = UUID.randomUUID();
        Lead lead = new Lead(randomUUID, "test@example.com", "+71234567890", "TestCorp", "NEW");

        // When
        UUID id = lead.getId();

        // Then
        assertThat(id).isEqualTo(randomUUID);
    }

    @Test
    void shouldReturnEmail_whenGetEmailCalled() {
        Lead lead = new Lead(UUID.randomUUID(), "tasty@example.com", "+71234567890", "TestCorp", "NEW");

        String email = lead.getEmail();

        assertThat(email).isEqualTo("tasty@example.com");
    }

    @Test
    void shouldReturnPhone_whenGetPhoneCalled() {
        Lead lead = new Lead(UUID.randomUUID(), "tasty@example.com", "+71234567890", "TestCorp", "NEW");

        String phone = lead.getPhone();

        assertThat(phone).isEqualTo("+71234567890");
        }

    @Test
    void shouldReturnCompany_whenGetCompanyCalled() {
        Lead lead = new Lead(UUID.randomUUID(), "tasty@example.com", "+71234567890", "TestCorp", "NEW");

        String company = lead.getCompany();

        assertThat(company).isEqualTo("TestCorp");
        }

    @Test
    void shouldReturnStatus_whenGetStatusCalled() {
        Lead lead = new Lead(UUID.randomUUID(), "tasty@example.com", "+71234567890", "TestCorp", "NEW");

        String status = lead.getStatus();

        assertThat(status).isEqualTo("NEW");
    }

    @Test
    void shouldReturnFormattedString_whenToStringCalled() {
        UUID randomUUID = UUID.randomUUID();
        Lead lead = new Lead(randomUUID, "tasty@example.com", "+71234567890", "TestCorp", "NEW");

        String formatStr = lead.toString();

        assertThat(formatStr).isEqualTo("Lead{id = " + randomUUID.toString() + ", email = tasty@example.com, phone = +71234567890, company = TestCorp, status = NEW }");
    }

    // В LeadTest
    @Test
    void shouldPreventStringConfusion_whenUsingUUID() {
        // TODO: создать Lead с UUID
        // TODO: попробовать создать метод findById(UUID id)
        // TODO: убедиться что передать String вместо UUID невозможно (ошибка компиляции)
        UUID randomUUID = UUID.randomUUID();
        Lead lead = new Lead(randomUUID, "tasty@example.com", "+71234567890", "TestCorp", "NEW");

        // Это не скомпилируется (демонстрация):
        // findById("some-string");  // ERROR: incompatible types

        // Это правильно:
        // findById(lead.id());  // OK: UUID to UUID
    }

    }


