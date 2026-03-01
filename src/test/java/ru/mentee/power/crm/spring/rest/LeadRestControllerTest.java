package ru.mentee.power.crm.spring.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.mentee.power.crm.testHelpClasses.DataGenerator.generateRandomLead;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.mentee.power.crm.spring.dto.LeadResponse;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.spring.mapper.LeadMapper;
import ru.mentee.power.crm.spring.service.JpaLeadService;

@WebMvcTest(LeadRestController.class)
class LeadRestControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private LeadMapper leadMapper;

  @MockitoBean private JpaLeadService leadService;

  @Test
  void shouldReturn200_whenGetAllLeads() throws Exception {
    List<Lead> leads = List.of(generateRandomLead(), generateRandomLead(), generateRandomLead());
    List<LeadResponse> response = leads.stream().map(leadMapper::toResponse).toList();

    mockMvc
        .perform(get("/api/leads"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"));
  }

  @Test
  void shouldReturn404_whenGetNonExistentLead() throws Exception {
    when(leadService.getLeadById(any())).thenReturn(null);
    mockMvc.perform(get("/api/leads/" + UUID.randomUUID())).andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn201WithLocation_whenCreateLead() throws Exception {
    Lead lead = generateRandomLead();
    when(leadService.createLead(any())).thenReturn(lead);

    String json =
        """
                {"name":"Max","email":"Max@gmail.com","phone":"8956897","status":"NEW"}
                """;

    mockMvc
        .perform(post("/api/leads").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isCreated())
        .andExpect(header().exists("location"))
        .andExpect(header().string("location", "/api/leads/" + lead.getId()));
  }

  @Test
  void shouldReturn204_whenDeleteExistingLead() throws Exception {
    UUID id = UUID.randomUUID();
    when(leadService.delete(id)).thenReturn(true);

    mockMvc
        .perform(delete("/api/leads/{id}", id))
        .andExpect(status().isNoContent())
        .andExpect(content().string(""));
  }

  @Test
  void shouldReturn404_whenDeleteNonExistentLead() throws Exception {
    UUID id = UUID.randomUUID();
    when(leadService.delete(id)).thenReturn(false);

    mockMvc.perform(delete("/api/leads/{id}", id)).andExpect(status().isNotFound());
  }
}
