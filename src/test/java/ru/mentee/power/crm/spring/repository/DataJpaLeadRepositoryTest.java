package ru.mentee.power.crm.spring.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.testHelpClasses.DataGenerator;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class DataJpaLeadRepositoryTest {

        @Autowired
        private JpaLeadRepository repository;

        @Test
        void shouldFindByEmailIgnoreCase_whenExists() {
            // Given
            Lead lead = DataGenerator.generateRandomLead();
            lead.setEmail("test@example.com");
            repository.save(lead);

            // When
            Optional<Lead> found = repository.findByEmailIgnoreCase("test@example.com");

            // Then
            assertThat(found).isPresent();
            assertThat(found.get().getEmail()).isEqualTo("test@example.com");
        }

        @Test
        void shouldReturnEmpty_whenEmailNotFound() {
            // When
            Optional<Lead> found = repository.findByEmailIgnoreCase("nonexistent@example.com");

            // Then
            assertThat(found).isEmpty();
        }
    }