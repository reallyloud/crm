package ru.mentee.power.crm.spring.rest;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.mentee.power.crm.spring.dto.generated.CreateLeadRequest;
import ru.mentee.power.crm.spring.dto.generated.LeadResponse;
import ru.mentee.power.crm.spring.dto.generated.UpdateLeadRequest;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.spring.mapper.LeadMapper;
import ru.mentee.power.crm.spring.rest.generated.LeadManagementApi;
import ru.mentee.power.crm.spring.service.JpaLeadService;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class LeadRestController implements LeadManagementApi {

  private final JpaLeadService service;
  private final LeadMapper leadMapper;

  public ResponseEntity<List<LeadResponse>> getLeads() {
    List<Lead> leads = service.findAll();
    List<LeadResponse> responses = leads.stream().map(leadMapper::toResponse).toList();
    return ResponseEntity.ok(responses);
  }

  public ResponseEntity<LeadResponse> getLeadById(@PathVariable("id") UUID id) {
    Optional<Lead> lead = service.findById(id);
    if (lead.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    LeadResponse response = leadMapper.toResponse(lead.get());
    return ResponseEntity.ok(response);
  }

  public ResponseEntity<LeadResponse> createLead(@Valid @RequestBody CreateLeadRequest lead) {
    Lead request = leadMapper.toEntity(lead);
    Lead createdLead = service.createLead(request);
    LeadResponse response = leadMapper.toResponse(createdLead);

    URI location = URI.create("/api/leads/" + createdLead.getId());

    return ResponseEntity.created(location).body(response);
  }

  public ResponseEntity<LeadResponse> updateLead(
      @PathVariable("id") UUID id, @Valid @RequestBody UpdateLeadRequest lead) {
    Optional<LeadResponse> updatedLead = service.updateLead(id, lead);

    return updatedLead
        .map(updated -> ResponseEntity.ok(updatedLead.get()))
        .orElse(ResponseEntity.notFound().build());
  }

  public ResponseEntity<Void> deleteLead(@PathVariable("id") UUID id) {
    boolean deleted = service.delete(id);
    if (deleted) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }
}
