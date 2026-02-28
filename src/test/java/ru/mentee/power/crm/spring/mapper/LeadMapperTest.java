package ru.mentee.power.crm.spring.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.mentee.power.crm.spring.dto.CreateLeadRequest;
import ru.mentee.power.crm.spring.dto.LeadResponse;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.testHelpClasses.DataGenerator;

@ActiveProfiles("test")
@SpringBootTest
class LeadMapperTest {

  @Autowired private LeadMapper leadMapper;

  @Test
  void shouldMapCreateRequestToEntity_whenValidData() {
    CreateLeadRequest request = new CreateLeadRequest("Oleg", "oleg@amgil.com", "89684396");
    Lead lead = leadMapper.toEntity(request);
    assertThat(lead.getName()).isEqualTo("Oleg");
    assertThat(lead.getEmail()).isEqualTo("oleg@amgil.com");
    assertThat(lead.getPhone()).isEqualTo("89684396");
    assertThat(lead.getId()).isNull();
  }

  @Test
  void shouldMapEntityToResponse_whenValidEntity() {
    Lead lead = DataGenerator.generateRandomLead();
    lead.setId(UUID.randomUUID());
    LeadResponse response = leadMapper.toResponse(lead);
    assertThat(response.id()).isEqualTo(lead.getId());
    assertThat(response.email()).isEqualTo(lead.getEmail());
    assertThat(response.phone()).isEqualTo(lead.getPhone());
    assertThat(response.name()).isEqualTo(lead.getName());
  }
}
