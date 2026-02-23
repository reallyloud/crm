// EmailValidationClient.java
package ru.mentee.power.crm.spring.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class EmailValidationClient {

  public Logger log = LoggerFactory.getLogger(EmailValidationClient.class);
  private final RestTemplate restTemplate;
  private final String baseUrl;

  public EmailValidationClient(
      RestTemplate restTemplate, @Value("${email.validation.base-url}") String baseUrl) {
    this.restTemplate = restTemplate;
    this.baseUrl = baseUrl;
  }

  public EmailValidationResponse validateEmail(String email) {
    String url = baseUrl + "/api/validate/email?email=" + email;
    try {
      EmailValidationResponse response =
          restTemplate.getForObject(url, EmailValidationResponse.class);
      if (response == null) {
        throw new EmailValidationException("Пустой ответ от сервиса валидации для email: " + email);
      }
      log.info(
          "Результат валидации email {}: valid={}, reason={}",
          email,
          response.valid(),
          response.reason());
      return response;
    } catch (RestClientException e) {
      log.error("Ошибка при валидации email {}: {}", email, e.getMessage());
      throw new EmailValidationException("Ошибка при валидации email: " + email);
    }
  }
}
