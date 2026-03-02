package ru.mentee.power.crm.spring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.mentee.power.crm.spring.dto.CreateInviteeRequest;
import ru.mentee.power.crm.spring.dto.InviteeResponse;
import ru.mentee.power.crm.spring.entity.Invitee;

@Mapper
public interface InviteeMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", expression = "java(InviteeStatus.INACTIVE)")
  Invitee toEntity(CreateInviteeRequest dto);

  InviteeResponse toResponse(Invitee entity);
}
