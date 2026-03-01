package ru.mentee.power.crm.spring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.mentee.power.crm.spring.dto.generated.CreateLeadRequest;
import ru.mentee.power.crm.spring.dto.generated.LeadResponse;
import ru.mentee.power.crm.spring.dto.generated.UpdateLeadRequest;
import ru.mentee.power.crm.spring.entity.Lead;

@Mapper
public interface LeadMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "company", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  Lead toEntity(CreateLeadRequest dto);

  LeadResponse toResponse(Lead entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "company", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  void updateEntity(UpdateLeadRequest dto, @MappingTarget Lead entity);
}
