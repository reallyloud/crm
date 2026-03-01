package ru.mentee.power.crm.spring.exception;

import lombok.Getter;

/**
 * Исключение выбрасывается когда запрошенная сущность не найдена. Маппится на HTTP 404 Not Found.
 */
@Getter
public class EntityNotFoundException extends BusinessException {

  private final String entityType;
  private final String entityId;

  public EntityNotFoundException(String entityType, String entityId) {
    super(entityType + " not found with id: " + entityId);
    this.entityId = entityId;
    this.entityType = entityType;
  }

  public EntityNotFoundException(String message) {
    super(message);
    this.entityType = null;
    this.entityId = null;
  }
}
