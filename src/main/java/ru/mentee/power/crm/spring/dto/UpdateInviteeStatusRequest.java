package ru.mentee.power.crm.spring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mentee.power.crm.spring.entity.InviteeStatus;

/** DTO для обновления статуса приглашённого пользователя. */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInviteeStatusRequest {

  @NotBlank(message = "Статус обязателен")
  private String status;

  /** Возвращает InviteeStatus из строки. */
  public InviteeStatus getStatusEnum() {
    return InviteeStatus.valueOf(status);
  }
}
