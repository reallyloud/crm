package ru.mentee.power.crm.spring.client;

public class EmailValidationException extends RuntimeException {

  public EmailValidationException(String message) {
    super(message);
  }

  public EmailValidationException(String message, Throwable cause) {
    super(message, cause);
  }
}
