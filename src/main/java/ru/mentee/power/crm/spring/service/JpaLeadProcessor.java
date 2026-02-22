package ru.mentee.power.crm.spring.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.spring.repository.JpaLeadRepository;

@RequiredArgsConstructor
@Service
public class JpaLeadProcessor {

  public final JpaLeadRepository repository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void processSingleLead(UUID id, int i) {
    if (i == 1) {
      throw new IllegalArgumentException();
    }
    if (repository.findById(id).isPresent()) {
      Lead lead = repository.findById(id).get();
      lead.setName("PROCESSED");
      repository.save(lead);
    }
  }
}
