package ru.mentee.power.crm.spring.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.service.LeadService;

@WebMvcTest(LeadController.class)
class LeadEditTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private LeadService leadService;

  @Test
  void getEditForm_withValidId_returnsEditViewWithLead() throws Exception {
    // Arrange
    UUID leadId = UUID.randomUUID();
    Lead expectedLead = new Lead(leadId, "test@example.com", "TestCompany", LeadStatus.NEW);

    when(leadService.findById(leadId)).thenReturn(Optional.of(expectedLead));

    // Act & Assert
    mockMvc
        .perform(get("/leads/{id}/edit", leadId))
        .andExpect(status().isOk())
        .andExpect(model().attribute("lead", expectedLead))
        .andExpect(view().name("spring/edit"));
    // Verify service was called
    Mockito.verify(leadService, atLeast(1)).findById(leadId);
  }

  @Test
  void postUpdateLeadUpdatesLeadAndRedirects() throws Exception {
    // Given
    UUID leadId = UUID.randomUUID();
    Lead existingLead = new Lead(leadId, "old@example.com", "OldCompany", LeadStatus.NEW);
    Lead updatedLead = new Lead(leadId, "new@example.com", "NewCompany", LeadStatus.CONTACTED);

    when(leadService.findById(leadId)).thenReturn(Optional.of(existingLead));

    // When
    mockMvc
        .perform(
            post("/leads/{id}", leadId)
                .param("email", "new@example.com")
                .param("company", "NewCompany")
                .param("status", "CONTACTED")
                .param("name", "name")
                .param("phone", "6789436"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/leads"));

    // Then
    Mockito.verify(leadService)
        .update(
            Mockito.eq(leadId.toString()),
            Mockito.argThat(
                lead ->
                    lead.email().equals("new@example.com")
                        && lead.company().equals("NewCompany")
                        && lead.status() == LeadStatus.CONTACTED));
  }

  @Test
  void getEditForm_withInvalidId_returns404() throws Exception {
    // Given
    UUID invalidId = UUID.randomUUID();

    when(leadService.findById(invalidId)).thenReturn(Optional.empty());

    // When
    mockMvc.perform(get("/leads/{id}/edit", invalidId)).andExpect(status().isNotFound());

    // Then
    Mockito.verify(leadService).findById(invalidId);
  }
}
