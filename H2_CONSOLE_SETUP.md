# Настройка H2 Console в Spring Boot 4.0

## Проблема
H2 Console не работала ни в тестах, ни при запуске через `bootRun`. В логах не появлялось сообщение "H2 console available at".

## Причина
В **Spring Boot 4.0** изменилась автоконфигурация. H2 Console теперь вынесена в отдельный модуль и требует явного добавления зависимости.

## Решение

### 1. Добавить зависимость H2 Console в build.gradle

```gradle
// H2 Database (для тестов и разработки)
testRuntimeOnly "com.h2database:h2:2.3.232"
runtimeOnly "com.h2database:h2:2.3.232"

// H2 Console (требуется для Spring Boot 4.0+)
implementation "org.springframework.boot:spring-boot-h2console:${springBootVersion}"
```

### 2. Настроить application-test.yml для тестов

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
    password:

  jpa:
    database: H2
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate:
        dialect: org.hibernate.dialect.H2Dialect

  h2:
    console:
      enabled: true
      path: /h2-console
```

### 3. Настроить application-dev.yml для разработки

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:devdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
    password:

  jpa:
    database: H2
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate:
        dialect: org.hibernate.dialect.H2Dialect

  sql:
    init:
      mode: always

  h2:
    console:
      enabled: true
      path: /h2-console
```

## Доступ к H2 Console

### При запуске тестов
H2 Console доступна на случайном порту (RANDOM_PORT). См. тест `H2ConsoleTest.java`.

### При запуске через bootRun
```
http://localhost:8081/h2-console
```

**Параметры подключения:**
- JDBC URL: `jdbc:h2:mem:devdb`
- User Name: `sa`
- Password: (оставить пустым)

## Важные замечания

1. **Spring Boot 4.0** требует явного добавления `spring-boot-h2console` в зависимости
2. H2 Console - это сервлет, а не Spring MVC контроллер
3. В тестах с `@WebMvcTest` H2 Console не будет доступна (нужен полный контекст с `@SpringBootTest`)
4. Для production рекомендуется использовать PostgreSQL (см. `application-dev.yml` с PostgreSQL настройками)

## Ссылки
- [Spring Boot 4.0 H2 Console Documentation](https://docs.spring.io/spring-boot/api/java/org/springframework/boot/autoconfigure/h2/H2ConsoleAutoConfiguration.html)
- [Medium: Enabling H2 Console in Spring Boot 4](https://medium.com/@bbakla/enabling-the-h2-console-in-spring-boot-4-7b658ad295fa)
