package ru.mentee.power.crm.spring.utility;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.spring.repository.JpaLeadRepository;

@RequiredArgsConstructor
@Service
public class Propagation {

  public final JpaLeadRepository leadRepository;

  @Transactional(
      propagation = org.springframework.transaction.annotation.Propagation.REQUIRED,
      rollbackFor = IllegalArgumentException.class)
  public Lead propagationRequired(UUID leadId, int attempt) {
    if (leadRepository.findById(leadId).isEmpty()) {
      throw new IllegalArgumentException("Лида с таким id не существует");
    }

    if (attempt == 2) {
      throw new IllegalArgumentException();
    }

    Lead lead = leadRepository.findById(leadId).get();

    lead.setName("PROCESSED");
    leadRepository.save(lead);
    return lead;
  }

  @Transactional(
      propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW,
      rollbackFor = IllegalArgumentException.class)
  public Lead propagationRequiresNew(UUID leadId, int attempt) {
    if (leadRepository.findById(leadId).isEmpty()) {
      throw new IllegalArgumentException("Лида с таким id не существует");
    }

    if (attempt == 2) {
      throw new IllegalArgumentException();
    }

    Lead lead = leadRepository.findById(leadId).get();

    lead.setName("PROCESSED");
    leadRepository.save(lead);
    return lead;
  }

  @Transactional(propagation = org.springframework.transaction.annotation.Propagation.MANDATORY)
  public Lead propagationMandatory(UUID leadId, int attempt) {
    if (leadRepository.findById(leadId).isEmpty()) {
      throw new IllegalArgumentException("Лида с таким id не существует");
    }

    if (attempt == 2) {
      throw new IllegalArgumentException();
    }

    Lead lead = leadRepository.findById(leadId).get();

    lead.setName("PROCESSED");
    leadRepository.save(lead);
    return lead;
  }

  @Transactional(
      propagation = org.springframework.transaction.annotation.Propagation.REQUIRED,
      isolation = Isolation.READ_COMMITTED)
  public Lead propagationRequiredIsolationCommitted(UUID leadId, int attempt) {
    if (leadRepository.findById(leadId).isEmpty()) {
      throw new IllegalArgumentException("Лида с таким id не существует");
    }
    Lead lead = leadRepository.findById(leadId).get();

    lead.setName("PROCESSED");
    leadRepository.save(lead);
    return lead;
  }

  @Transactional(
      propagation = org.springframework.transaction.annotation.Propagation.REQUIRED,
      isolation = Isolation.REPEATABLE_READ)
  public Lead propagationRequiredIsolationRepeatable(UUID leadId, int attempt) {
    if (leadRepository.findById(leadId).isEmpty()) {
      throw new IllegalArgumentException("Лида с таким id не существует");
    }

    Lead lead = leadRepository.findById(leadId).get();

    lead.setName("PROCESSED");
    leadRepository.save(lead);
    return lead;
  }
}
