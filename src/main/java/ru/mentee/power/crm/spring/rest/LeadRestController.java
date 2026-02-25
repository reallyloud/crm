package ru.mentee.power.crm.spring.rest;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.spring.service.JpaLeadService;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class LeadRestController {

  Logger log = LoggerFactory.getLogger(LeadRestController.class);

  private final JpaLeadService service;

  @GetMapping
  public ResponseEntity<List<Lead>> getAllLeads() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Lead> getLeadById(@PathVariable UUID id) {
    Optional<Lead> lead = service.findById(id);
    if (lead.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(lead.get());
  }

  @PostMapping
  public ResponseEntity<Lead> createLead(@RequestBody Lead lead) {
    Lead createdLead = service.createLead(lead);
    URI location = URI.create("/api/leads/" + createdLead.getId());
    return ResponseEntity.created(location).body(createdLead);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Lead> updateLead(@PathVariable UUID id, @RequestBody Lead lead) {
    Optional<Lead> updatedLead = service.updateLead(id, lead);
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
