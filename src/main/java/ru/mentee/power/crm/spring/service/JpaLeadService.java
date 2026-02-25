package ru.mentee.power.crm.spring.service;

import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.client.EmailValidationFeignClient;
import ru.mentee.power.crm.spring.client.EmailValidationResponse;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.spring.repository.JpaCompanyRepository;
import ru.mentee.power.crm.spring.repository.JpaLeadRepository;

@Service
@RequiredArgsConstructor
public class JpaLeadService {

  private static final Logger log = LoggerFactory.getLogger(JpaLeadService.class);

  private final JpaLeadRepository repository;
  private final JpaLeadProcessor processor;
  private final JpaCompanyRepository companyRepository;
  private final EmailValidationFeignClient emailValidationClient;

  /** Поиск лида по email (derived method). */
  public Optional<Lead> findByEmail(String email) {
    return repository.findByEmail(email);
  }

  /** Поиск лидов по списку статусов (JPQL). */
  public List<Lead> findByStatuses(LeadStatus... statuses) {
    return repository.findByStatusIn(List.of(statuses));
  }

  /** Получить первую страницу лидов с сортировкой. */
  public Page<Lead> getFirstPage(int pageSize) {
    PageRequest pageRequest =
        PageRequest.of(
            0, // первая страница (нумерация с 0)
            pageSize,
            Sort.by("createdAt").descending());
    return repository.findAll(pageRequest);
  }

  public Page<Lead> searchByCompany(String company, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return repository.findByCompany(company, pageable);
  }

  /**
   * Массовое обновление статуса (используется @Modifying метод). ВАЖНО: @Transactional обязательна
   * для @Modifying!
   */
  @Transactional
  public int convertNewToContacted() {
    int updated = repository.updateStatusBulk(LeadStatus.NEW, LeadStatus.CONTACTED);
    // Логируем для observability
    log.info("Converted {} leads from NEW to CONTACTED", updated);
    return updated;
  }

  @Transactional
  public int archiveOldLeads(LeadStatus status) {
    return repository.deleteByStatusBulk(status);
  }

  // SELF INVOKE PROBLEM
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  private void processSingleLead(UUID id, int i) {
    if (i == 1) {
      throw new IllegalArgumentException();
    }
    if (repository.findById(id).isPresent()) {
      Lead lead = repository.findById(id).get();
      lead.setName("PROCESSED");
      repository.save(lead);
    }
  }

  // SELF INVOKE PROBLEM
  public void processLeadsSelfInvoke(List<UUID> ids) {
    int i = 0;
    for (UUID id : ids) {
      this.processSingleLead(id, i);
      i++;
    }
  }

  public void processLeads(List<UUID> ids) {
    int i = 0;
    for (UUID id : ids) {
      processor.processSingleLead(id, i);
      i++;
    }
  }

  public List<Lead> findAll() {
    List<Lead> leads = repository.findAll();
    if (leads.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Leads not found.");
    }
    return leads;
  }

  public Optional<Lead> getLeadById(UUID id) {
    return repository.findById(id);
  }

  @Retry(name = "email-validation", fallbackMethod = "createLeadFallback")
  public Lead createLead(Lead lead) {
    log.info("Создание нового лида из JSON: {}", lead.toString());

    EmailValidationResponse validation = emailValidationClient.validateEmail(lead.getEmail());
    if (!validation.valid()) {
      throw new IllegalArgumentException("Invalid email: " + validation.reason());
    }
    lead.setStatus(LeadStatus.NEW);
    return repository.save(lead);
  }

  // Fallback метод — вызывается после исчерпания retry попыток
  public Lead createLeadFallback(Lead lead, Exception ex) {
    log.warn(
        "Email validation service unavailable after retries. "
            + "Creating lead without validation. Error: {}",
        ex.getMessage());
    return repository.save(lead);
  }

  public Optional<Lead> findById(UUID id) {
    return repository.findById(id);
  }

  @Transactional
  public Lead save(Lead lead) {
    return repository.save(lead);
  }

  @Transactional
  public boolean delete(UUID id) {
    boolean isDeleted = repository.existsById(id);
    repository.deleteById(id);
    return isDeleted;
  }

  @Transactional
  public Optional<Lead> updateLead(UUID id, Lead updatedLead) {
    Optional<Lead> foundLead = repository.findById(id);
    if (foundLead.isEmpty()) {
      return foundLead;
    }
    Lead lead = foundLead.get();
    lead.setName(updatedLead.getName());
    lead.setEmail(updatedLead.getEmail());
    lead.setPhone(updatedLead.getPhone());
    lead.setStatus(updatedLead.getStatus());
    repository.save(lead);
    return Optional.of(lead);
  }

  public List<Lead> findLeads(String search, String status) {
    List<Lead> leads = repository.findAll();
    Stream<Lead> stream = leads.stream();
    if (search != null && !search.isEmpty()) {
      String lowerSearch = search.toLowerCase();
      stream =
          stream.filter(
              l ->
                  l.getEmail().toLowerCase().contains(lowerSearch)
                      || l.getName().toLowerCase().contains(lowerSearch));
    }
    if (status != null && !status.isEmpty()) {
      try {
        LeadStatus leadStatus = LeadStatus.valueOf(status);
        stream = stream.filter(l -> l.getStatus() == leadStatus);
      } catch (IllegalArgumentException ignored) {
      }
    }
    return stream.collect(Collectors.toList());
  }

  @Transactional
  public int changePhoneByCompany(UUID id, String phone) {
    return repository.updatePhoneBulk(id, phone);
  }
}
