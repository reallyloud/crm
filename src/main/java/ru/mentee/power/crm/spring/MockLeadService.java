package ru.mentee.power.crm.spring;

import java.util.*;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.service.LeadService;

public class MockLeadService extends LeadService {
  private final List<Lead> mockLeads;

  public MockLeadService() {
    super(null); // repository не используется в mock
    this.mockLeads =
        List.of(
            new Lead(UUID.randomUUID(), "test1@example.com", "company1", LeadStatus.NEW),
            new Lead(UUID.randomUUID(), "test2@example.com", "company2", LeadStatus.NEW));
  }

  @Override
  public List<Lead> findAll() {
    return mockLeads;
  }
}
