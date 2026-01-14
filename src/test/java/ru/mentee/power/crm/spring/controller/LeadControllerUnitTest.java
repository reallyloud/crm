package ru.mentee.power.crm.spring.controller;

import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.spring.MockLeadService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class LeadControllerUnitTest {

    @Test
    void shouldCreateControllerWithoutSpring() {
        // Given: mock service без Spring контейнера
        MockLeadService mockService = new MockLeadService();

        // When: создаём контроллер через конструктор (pure Java)
        LeadController controller = new LeadController(mockService);

        // Then: контроллер работает, использует mock service
        List<Lead> response = controller.getService().findAll();
        assertThat(response).hasSize(2); // MockLeadService возвращает 2 лида
    }

    @Test
    void shouldUseInjectedService() {
        // Given
        MockLeadService mockService = new MockLeadService();
        LeadController controller = new LeadController(mockService);

        // When: вызываем метод контроллера
        List<Lead> response = controller.getService().findAll();

        // Then: сервис использован (не null)
        assertThat(response).isNotNull();}
}