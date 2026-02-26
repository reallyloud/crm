package ru.mentee.power.crm.spring.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.mentee.power.crm.model.LeadStatus;

@Getter
@Setter
@AllArgsConstructor
public class UpdateLeadRequest {
  @NotBlank UUID id;

  private String name;

  private String email;

  private String phone;

  private LeadStatus status;
}
