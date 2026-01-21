package ru.mentee.power.crm.spring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.controller.LeadController;
import ru.mentee.power.crm.spring.repository.InMemoryLeadRepository;
import ru.mentee.power.crm.spring.service.LeadService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LeadController.class)
class LeadControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private LeadService leadService;

    @Test
    void shouldDeleteLeadAndRedirect() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(post("/leads/{id}/delete", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/leads"));

        verify(leadService).delete(id);
    }

    @Test
    void shouldFindOnlyOneLead () throws Exception {
        Lead lead = new Lead(UUID.randomUUID(),"email@gmail.com","yandex",LeadStatus.NEW);
        List<Lead> leadList = new ArrayList<>();
        leadList.add(lead);

        when(leadService.findLeads("ivan",null)).thenReturn(leadList);

        mockMvc.perform(get("/leads")
                .param("search","ivan"))
                .andExpect(model().attribute("leads",leadList))
                .andExpect(status().isOk());

        verify(leadService).findLeads("ivan",null);
    }

    @Test
    void shouldReturnOnlyWithNewStatus() throws Exception {
        LeadService realService = new LeadService(new InMemoryLeadRepository());
        realService.clear();

        realService.addLead("email1@gmail.com","yandex1",LeadStatus.CONTACTED);
        realService.addLead("email2@gmail.com","yandex2",LeadStatus.CONTACTED);
        realService.addLead("email3@gmail.com","yandex3",LeadStatus.NEW);
        realService.addLead("email4@gmail.com","yandex4",LeadStatus.NEW);
        realService.addLead("email5@gmail.com","yandex5",LeadStatus.NEW);
        List<Lead> expectedLeads = realService.findLeads(null,"NEW");

        when(leadService.findLeads(null,"NEW"))
                .thenReturn(realService.findLeads(null,"NEW"));


        mockMvc.perform(get("/leads")
                        .param("status","NEW"))
                .andExpect(model().attribute("leads",expectedLeads))
                .andExpect(status().isOk());
    }

    @Test
    void shouldWorkWithBothFilters() throws Exception {
        //GIVEN
        LeadService realService = new LeadService(new InMemoryLeadRepository());
        realService.clear();

        //Нужные лиды
        realService.addLead("email3@gmail.com","yandex3",LeadStatus.NEW);
        realService.addLead("email34@gmail.com","yandex3",LeadStatus.NEW);
        realService.addLead("email344@gmail.com","yandex3",LeadStatus.CONTACTED);
        //Остальные лиды для проверки
        realService.addLead("email1@gmail.com","yandex1",LeadStatus.CONTACTED);
        realService.addLead("email2@gmail.com","yandex2",LeadStatus.CONTACTED);
        realService.addLead("email4@gmail.com","yandex4",LeadStatus.NEW);
        realService.addLead("email5@gmail.com","yandex5",LeadStatus.NEW);

        List<Lead> expectedLeads = realService.findLeads("email3","NEW");
        //Мокируем метод leadService
        when(leadService.findLeads("email3","NEW"))
                .thenReturn(expectedLeads);

        //WHEN,THEN
        mockMvc.perform(get("/leads")
                        .param("search","email3")
                        .param("status","NEW"))
                .andExpect(model().attribute("leads",expectedLeads))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnFormWithFieldErrorName() throws Exception {
        mockMvc.perform(post("/leads")
                .param("name", "").param("email", "test@test.com"))
                .andExpect(view().name("leads/createForm"))
                .andExpect(model().attributeHasFieldErrors("lead", "name"));
    }

    @Test
    void shouldReturnFormWithFieldErrorEmail() throws Exception {
        mockMvc.perform(post("/leads")
                .param("email", "invalidemail"))
                .andExpect(model().attributeHasFieldErrorCode("lead", "email", "Email"));
    }

    @Test
    void allFieldsValid() throws Exception {
        mockMvc.perform(post("/leads")
                .param("email", "example@gmial.com")
                .param("status","NEW")
                .param("phone","52364")
                .param("name","name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/leads"));

    }






}