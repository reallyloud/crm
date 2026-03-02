package ru.mentee.power.crm.spring.rest.fixed;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mentee.power.crm.spring.dto.CreateInviteeRequest;
import ru.mentee.power.crm.spring.dto.InviteeResponse;
import ru.mentee.power.crm.spring.dto.UpdateInviteeStatusRequest;
import ru.mentee.power.crm.spring.entity.Invitee;
import ru.mentee.power.crm.spring.mapper.InviteeMapper;
import ru.mentee.power.crm.spring.service.InviteeService;

/**
 * REST контроллер для управления приглашёнными пользователями. Содержит только HTTP-слой логику;
 * бизнес-логика делегирована в InviteeService.
 */
@Slf4j
@RestController
@RequestMapping("/api/invitees")
@RequiredArgsConstructor
@Validated
public class InviteeController {

  private final InviteeService service;
  private final InviteeMapper mapper;

  /**
   * Invitee с пагинацией.
   *
   * @param pageable пагинация
   * @return список DTO Response
   */
  @GetMapping
  public ResponseEntity<List<InviteeResponse>> getInvitees(
      @PageableDefault(sort = "createdAt") Pageable pageable) {
    Page<Invitee> page = service.findAll(pageable);
    List<InviteeResponse> responses = page.getContent().stream().map(mapper::toResponse).toList();
    return ResponseEntity.ok(responses);
  }

  /**
   * Возвращает приглашённого пользователя по ID.
   *
   * @param id UUID
   * @return InviteeResponse или 404
   */
  @GetMapping("/{id}")
  public ResponseEntity<InviteeResponse> getById(@PathVariable UUID id) {
    Invitee invitee = service.findById(id);
    return ResponseEntity.ok(mapper.toResponse(invitee));
  }

  /**
   * Создать нового invitee
   *
   * @param request DTO создания
   * @return 201 Created с Location header
   */
  @PostMapping
  public ResponseEntity<InviteeResponse> create(@Valid @RequestBody CreateInviteeRequest request) {
    Invitee created = service.create(request);
    InviteeResponse response = mapper.toResponse(created);
    URI location = URI.create("/api/invitees/" + created.getId());
    return ResponseEntity.created(location).body(response);
  }

  /**
   * Удалить invitee.
   *
   * @param id UUID
   * @return 204 No Content или 404
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Обновить invitee.
   *
   * @param id UUID
   * @param request DTO со статусом
   * @return обновлённый InviteeResponse
   */
  @PutMapping("/{id}/status")
  public ResponseEntity<InviteeResponse> updateStatus(
      @PathVariable UUID id, @Valid @RequestBody UpdateInviteeStatusRequest request) {
    Invitee updated = service.updateStatus(id, request);
    return ResponseEntity.ok(mapper.toResponse(updated));
  }
}
