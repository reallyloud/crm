package ru.mentee.power.crm.spring.exception;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.mentee.power.crm.spring.mapper.LeadMapper;
import ru.mentee.power.crm.spring.rest.LeadRestController;
import ru.mentee.power.crm.spring.service.JpaLeadService;

@ActiveProfiles("test")
@WebMvcTest(LeadRestController.class)
class GlobalExceptionHandlerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private JpaLeadService service;

  @MockitoBean private LeadMapper mapper;

  @Test
  void shouldReturn404_whenEntityNotFound() throws Exception {
    UUID leadId = UUID.randomUUID();
    when(service.findById(any())).thenThrow(new EntityNotFoundException("Lead", leadId.toString()));

    mockMvc
        .perform(get("/api/leads/" + leadId))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(404));
  }

  @Test
  void shouldReturn400WithFieldErrors_whenValidationFails() throws Exception {
    String json =
        """
                {"email": "", "name": "", "phone": ""}
                """;
    mockMvc
        .perform(post("/api/leads").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn500_whenUnexpectedExceptionOccurs() throws Exception {
    UUID leadId = UUID.randomUUID();
    when(service.findById(any())).thenThrow(new RuntimeException());

    mockMvc.perform(get("/api/leads/" + leadId)).andExpect(status().isInternalServerError());
  }
}
