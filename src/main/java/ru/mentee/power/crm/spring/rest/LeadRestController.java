package ru.mentee.power.crm.spring.rest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  public List<Lead> getAllLeads() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public Lead getLeadById(@PathVariable UUID id) {
    Optional<Lead> lead = service.findById(id);
    return lead.orElse(null);
  }

  @PostMapping
  public Lead createLead(@RequestBody Lead lead) {
    return service.createLead(lead);
  }
}
