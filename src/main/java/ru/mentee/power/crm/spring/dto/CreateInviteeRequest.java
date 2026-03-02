package ru.mentee.power.crm.spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** DTO для создания приглашённого пользователя. */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateInviteeRequest {

  @NotBlank(message = "Email обязателен")
  @Email(message = "Email должен быть в корректном формате")
  private String email;

  @NotBlank(message = "Имя обязательно")
  @Size(min = 1, max = 255)
  private String firstName;
}
