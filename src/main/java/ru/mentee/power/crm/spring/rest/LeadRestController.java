package ru.mentee.power.crm.spring.rest;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mentee.power.crm.spring.dto.CreateLeadRequest;
import ru.mentee.power.crm.spring.dto.LeadResponse;
import ru.mentee.power.crm.spring.dto.UpdateLeadRequest;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.spring.mapper.LeadMapper;
import ru.mentee.power.crm.spring.service.JpaLeadService;

@Slf4j
@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class LeadRestController {

  private final JpaLeadService service;
  private final LeadMapper leadMapper;

  @GetMapping
  public ResponseEntity<List<LeadResponse>> getAllLeads() {
    List<Lead> leads = service.findAll();
    List<LeadResponse> responses = leads.stream().map(leadMapper::toResponse).toList();
    return ResponseEntity.ok(responses);
  }

  @GetMapping("/{id}")
  public ResponseEntity<LeadResponse> getLeadById(@PathVariable UUID id) {
    Optional<Lead> lead = service.findById(id);
    if (lead.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    LeadResponse response = leadMapper.toResponse(lead.get());
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<LeadResponse> createLead(@RequestBody CreateLeadRequest lead) {
    Lead request = leadMapper.toEntity(lead);
    Lead createdLead = service.createLead(request);
    LeadResponse response = leadMapper.toResponse(createdLead);

    URI location = URI.create("/api/leads/" + createdLead.getId());

    return ResponseEntity.created(location).body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<LeadResponse> updateLead(
      @PathVariable UUID id, @RequestBody UpdateLeadRequest lead) {
    Optional<LeadResponse> updatedLead = service.updateLead(id, lead);

    return updatedLead
        .map(updated -> ResponseEntity.ok(updatedLead.get()))
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteLead(@PathVariable UUID id) {
    boolean deleted = service.delete(id);
    if (deleted) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }
}
