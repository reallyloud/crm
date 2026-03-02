package ru.mentee.power.crm.spring.service;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.spring.dto.CreateInviteeRequest;
import ru.mentee.power.crm.spring.dto.UpdateInviteeStatusRequest;
import ru.mentee.power.crm.spring.entity.Invitee;
import ru.mentee.power.crm.spring.exception.EntityNotFoundException;
import ru.mentee.power.crm.spring.mapper.InviteeMapper;
import ru.mentee.power.crm.spring.repository.JpaInviteeRepository;

/**
 * Сервис бизнес-логики для приглашённых пользователей. Выполняет все операции с Invitee, контроллер
 * делегирует только HTTP-слой.
 */
@Service
public class InviteeService {

  private final JpaInviteeRepository repository;
  private final InviteeMapper mapper;

  public InviteeService(JpaInviteeRepository repository, InviteeMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  public Page<Invitee> findAll(Pageable pageable) {
    return repository.findAll(pageable);
  }

  public Invitee findById(UUID id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Invitee", id.toString()));
  }

  @Transactional
  public Invitee create(CreateInviteeRequest request) {
    if (repository.existsByEmail(request.getEmail())) {
      throw new IllegalArgumentException(
          "Invitee с email " + request.getEmail() + " уже существует");
    }
    Invitee entity = mapper.toEntity(request);
    return repository.save(entity);
  }

  @Transactional
  public void delete(UUID id) {
    Invitee invitee =
        repository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Invitee", id.toString()));
    repository.delete(invitee);
  }

  @Transactional
  public Invitee updateStatus(UUID id, UpdateInviteeStatusRequest request) {
    Invitee invitee =
        repository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Invitee", id.toString()));
    invitee.setStatus(request.getStatusEnum());
    return repository.save(invitee);
  }
}
