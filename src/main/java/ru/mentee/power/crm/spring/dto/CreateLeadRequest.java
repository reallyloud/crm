package ru.mentee.power.crm.spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateLeadRequest {
  @Size(min = 2)
  @NotBlank
  private String name;

  @NotBlank(message = "Email обязателен")
  @Email(message = "Email должен быть в корректном формате")
  private String email;

  @NotBlank
  @Size(min = 7, message = "Телефон не может быть короче 7 цифр")
  private String phone;
}
