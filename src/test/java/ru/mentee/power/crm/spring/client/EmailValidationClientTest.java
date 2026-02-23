package ru.mentee.power.crm.spring.client;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@ActiveProfiles("test")
@SpringBootTest
class EmailValidationClientWireMockTest {

  @RegisterExtension
  static WireMockExtension wireMock =
      WireMockExtension.newInstance()
          .options(WireMockConfiguration.wireMockConfig().dynamicPort())
          .build();

  @Autowired private EmailValidationClient emailValidationClient;

  // @DynamicPropertySource — аннотация Spring Framework, позволяющая программно добавлять свойства
  // в Environment

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {

    // Регистрируем свойство с ключом email.validation.base-url. Значением будет результат вызова
    // wireMock.baseUrl()
    registry.add("email.validation.base-url", wireMock::baseUrl);
  }

  @Test
  void shouldReturnValid_whenEmailIsCorrect() {
    // Given: WireMock stub возвращает valid=true
    wireMock.stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .withQueryParam("email", equalTo("john@example.com"))
            .willReturn(
                okJson(
                    """
                        {
                            "email": "john@example.com",
                            "valid": true,
                            "reason": "Email exists"
                        }
                        """)));

    // When: вызываем клиент
    EmailValidationResponse response = emailValidationClient.validateEmail("john@example.com");

    // Then: получаем корректный response
    assertThat(response).isNotNull();
    assertThat(response.valid()).isTrue();
    assertThat(response.email()).isEqualTo("john@example.com");
  }

  @Test
  void shouldReturnInvalid_whenEmailIsIncorrect() {
    // Given: WireMock stub возвращает valid=false
    wireMock.stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .withQueryParam("email", equalTo("invalid-email"))
            .willReturn(
                okJson(
                    """
                        {
                            "email": "invalid-email",
                            "valid": false,
                            "reason": "Invalid email format"
                        }
                        """)));

    // When: вызываем клиент
    EmailValidationResponse response = emailValidationClient.validateEmail("invalid-email");

    // Then: email невалиден
    assertThat(response).isNotNull();
    assertThat(response.valid()).isFalse();
  }

  @Test
  void shouldHandleServerError_whenExternalServiceFails() {
    // Given: WireMock stub возвращает 500 Internal Server Error
    wireMock.stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .willReturn(serverError().withBody("Internal Server Error")));

    assertThatThrownBy(() -> emailValidationClient.validateEmail("randomEmail@mail.ru"))
        .isInstanceOf(EmailValidationException.class);
  }

  @Test
  void shouldHandleTimeout_whenExternalServiceIsSlow() {
    // Given: WireMock stub отвечает с задержкой 15 секунд (больше timeout)
    wireMock.stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .willReturn(okJson("{\"valid\": true}").withFixedDelay(15000))); // 15 секунд

    assertThatThrownBy(() -> emailValidationClient.validateEmail("randomEmail@mail.ru"))
        .isInstanceOf(EmailValidationException.class)
        .hasMessageContaining("Ошибка при валидации email: randomEmail@mail.ru");
  }
}
