package ru.mentee.power.crm.spring.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.mentee.power.crm.spring.dto.CreateLeadRequest;
import ru.mentee.power.crm.spring.service.JpaLeadService;
import ru.mentee.power.crm.testHelpClasses.DataGenerator;
import tools.jackson.databind.json.JsonMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LeadRestControllerValidationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private JsonMapper jsonMapper;

  @MockitoBean private JpaLeadService leadService;

  @Test
  void shouldReturn400_whenEmailIsBlank() throws Exception {
    // Given: CreateLeadRequest с пустым email
    CreateLeadRequest request = new CreateLeadRequest();
    request.setEmail(""); // Пустой email
    request.setName("John");
    request.setPhone("896905489789");

    String requestJson = jsonMapper.writeValueAsString(request);

    mockMvc
        .perform(post("/api/leads").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn400_whenEmailIsInvalidFormat() throws Exception {

    CreateLeadRequest request = new CreateLeadRequest();
    request.setEmail("not-an-email"); // Пустой email
    request.setName("John");
    request.setPhone("896905489789");

    String requestJson = jsonMapper.writeValueAsString(request);

    mockMvc
        .perform(post("/api/leads").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn400_whenFirstNameIsTooShort() throws Exception {

    CreateLeadRequest request = new CreateLeadRequest();
    request.setEmail("email@gmail.com"); // Пустой email
    request.setName("J");
    request.setPhone("896905489789");

    String requestJson = jsonMapper.writeValueAsString(request);

    mockMvc
        .perform(post("/api/leads").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn201_whenAllFieldsAreValid() throws Exception {

    CreateLeadRequest request = new CreateLeadRequest();
    request.setEmail("email@gmail.com"); // Пустой email
    request.setName("John");
    request.setPhone("896905489789");

    when(leadService.createLead(any())).thenReturn(DataGenerator.generateRandomLead());

    String requestJson = jsonMapper.writeValueAsString(request);

    mockMvc
        .perform(post("/api/leads").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isCreated());
  }
}
