package ru.mentee.power.crm.spring.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.entity.Company;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.spring.service.JpaLeadService;

@WebMvcTest(JpaLeadController.class)
class JpaLeadControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private JpaLeadService leadService;

  @Test
  void shouldListLeads() throws Exception {
    Lead lead = createTestLead();
    when(leadService.findLeads(null, null)).thenReturn(List.of(lead));

    mockMvc
        .perform(get("/jpa/leads"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("leads"))
        .andExpect(view().name("jpa/leads/list"));

    verify(leadService).findLeads(null, null);
  }

  @Test
  void shouldFilterLeadsBySearch() throws Exception {
    Lead lead = createTestLead();
    when(leadService.findLeads("test", null)).thenReturn(List.of(lead));

    mockMvc
        .perform(get("/jpa/leads").param("search", "test"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("search", "test"))
        .andExpect(view().name("jpa/leads/list"));

    verify(leadService).findLeads("test", null);
  }

  @Test
  void shouldFilterLeadsByStatus() throws Exception {
    Lead lead = createTestLead();
    when(leadService.findLeads(null, "NEW")).thenReturn(List.of(lead));

    mockMvc
        .perform(get("/jpa/leads").param("status", "NEW"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("status", "NEW"))
        .andExpect(view().name("jpa/leads/list"));

    verify(leadService).findLeads(null, "NEW");
  }

  @Test
  void shouldFilterLeadsBySearchAndStatus() throws Exception {
    Lead lead = createTestLead();
    when(leadService.findLeads("test", "NEW")).thenReturn(List.of(lead));

    mockMvc
        .perform(get("/jpa/leads").param("search", "test").param("status", "NEW"))
        .andExpect(status().isOk())
        .andExpect(view().name("jpa/leads/list"));

    verify(leadService).findLeads("test", "NEW");
  }

  @Test
  void shouldShowCreateForm() throws Exception {
    mockMvc
        .perform(get("/jpa/leads/new"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("lead"))
        .andExpect(view().name("jpa/leads/create"));
  }

  @Test
  void shouldCreateLeadAndRedirect() throws Exception {
    when(leadService.save(any(Lead.class))).thenReturn(createTestLead());

    mockMvc
        .perform(
            post("/jpa/leads")
                .param("name", "Иван")
                .param("email", "ivan@test.com")
                .param("phone", "89534895678")
                .param("companyName", "Яндекс")
                .param("status", "NEW"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/jpa/leads"));

    verify(leadService).save(any(Lead.class));
  }

  @Test
  void shouldReturnFormWithFieldErrorName() throws Exception {
    mockMvc
        .perform(
            post("/jpa/leads")
                .param("name", "")
                .param("email", "test@test.com")
                .param("phone", "12345")
                .param("status", "NEW"))
        .andExpect(view().name("jpa/leads/createForm"))
        .andExpect(model().attributeHasFieldErrors("lead", "name"));
  }

  @Test
  void shouldReturnFormWithFieldErrorEmail() throws Exception {
    mockMvc
        .perform(
            post("/jpa/leads")
                .param("name", "Тест")
                .param("email", "invalidemail")
                .param("phone", "12345")
                .param("status", "NEW"))
        .andExpect(model().attributeHasFieldErrorCode("lead", "email", "Email"));
  }

  @Test
  void shouldReturnFormWithFieldErrorPhone() throws Exception {
    mockMvc
        .perform(
            post("/jpa/leads")
                .param("name", "Тест")
                .param("email", "test@test.com")
                .param("phone", "")
                .param("status", "NEW"))
        .andExpect(view().name("jpa/leads/createForm"))
        .andExpect(model().attributeHasFieldErrors("lead", "phone"));
  }

  @Test
  void shouldShowEditForm() throws Exception {
    UUID id = UUID.randomUUID();
    Lead lead = createTestLead();
    lead.setId(id);
    when(leadService.findById(id)).thenReturn(Optional.of(lead));

    mockMvc
        .perform(get("/jpa/leads/{id}/edit", id))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("lead"))
        .andExpect(model().attributeExists("leadId"))
        .andExpect(view().name("jpa/leads/edit"));
  }

  @Test
  void shouldReturn404WhenEditingNonExistentLead() throws Exception {
    UUID id = UUID.randomUUID();
    when(leadService.findById(id)).thenReturn(Optional.empty());

    mockMvc.perform(get("/jpa/leads/{id}/edit", id)).andExpect(status().isNotFound());
  }

  @Test
  void shouldUpdateLeadAndRedirect() throws Exception {
    UUID id = UUID.randomUUID();
    Lead lead = createTestLead();
    lead.setId(id);
    when(leadService.update(eq(id), any(), any(), any(), any(), any())).thenReturn(lead);

    mockMvc
        .perform(
            post("/jpa/leads/{id}", id)
                .param("name", "Обновленный")
                .param("email", "updated@test.com")
                .param("phone", "89999999")
                .param("companyName", "Google")
                .param("status", "CONTACTED"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/jpa/leads"));

    verify(leadService)
        .update(
            eq(id),
            eq("Обновленный"),
            eq("updated@test.com"),
            eq("89999999"),
            eq("Google"),
            eq(LeadStatus.CONTACTED));
  }

  @Test
  void shouldDeleteLeadAndRedirect() throws Exception {
    UUID id = UUID.randomUUID();

    mockMvc
        .perform(post("/jpa/leads/{id}/delete", id))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/jpa/leads"));

    verify(leadService).delete(id);
  }

  private Lead createTestLead() {
    Company company = new Company();
    company.setName("TestCompany");
    Lead lead = new Lead("Тест", "test@test.com", company, LeadStatus.NEW);
    lead.setPhone("89534895678");
    return lead;
  }
}
