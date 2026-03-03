[![CI](https://github.com/reallyloud/crm/actions/workflows/ci.yml/badge.svg)](https://github.com/reallyloud/crm/actions/workflows/ci.yml)
                            

# Сравнение стеков Servlet vs Spring Boot
                            


## Результаты интеграционного теста


| Метрика | Servlet | Spring Boot | Комментарий |
|---------|---------|-------------|-------------|
| Время старта | ~350 ms | ~2700 ms    | Spring загружает IoC контейнер |
| HTTP 200 на /leads | ✅       | ✅           | Оба работают идентично |
| Количество лидов | N       | N           | Данные одинаковые |
| Строк Java кода | ~150    | ~30         | Контраст 5:1 |



## Вывод


Оба стека возвращают идентичные данные, но Spring Boot требует в 5 раз меньше кода за счёт auto-configuration. Trade-off: Spring стартует медленнее из-за инициализации IoC контейнера.

*Данные получены из `StackComparisonTest.java`*


## Класс Invitee для код-ревью. 

package ru.mentee.power.crm.spring.rest.problematic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mentee.power.crm.domain.Invitee;
import ru.mentee.power.crm.spring.repository.InviteeRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/ **
* ЗАДАНИЕ: Найдите все проблемы в этом контроллере используя чек-лист.
* Ожидается найти минимум 10 проблем из разных категорий.
  */
  @RestController
  public class InviteeController {

  @Autowired
  InviteeRepository repository;

  // TODO: Студент должен найти проблемы в этом методе
  @PostMapping("/getInvitees")
  public List<Invitee> getInvitees() {
  return repository.findAll();
  }

  // TODO: Студент должен найти проблемы в этом методе
  @GetMapping("/invitees/{id}")
  public Invitee getById(@PathVariable UUID id) {
  return repository.findById(id).orElse(null);
  }

  // TODO: Студент должен найти проблемы в этом методе
  @PostMapping("/invitees")
  public Invitee create(@RequestBody Map<String, Object> params) {
  String email = (String) params.get("email");
  String firstName = (String) params.get("firstName");

       // Проверка email через SQL
       String sql = "SELECT COUNT(*) FROM invitees WHERE email = '" + email + "'";
       // repository.executeNativeQuery(sql); // Представим что это выполняется

       Invitee invitee = new Invitee();
       invitee.setId(UUID.randomUUID());
       invitee.setEmail(email);
       invitee.setFirstName(firstName);
       invitee.setCreatedAt(Instant.now());

       return repository.save(invitee);
  }

  // TODO: Студент должен найти проблемы в этом методе
  @DeleteMapping("/invitees/{id}")
  public Invitee delete(@PathVariable UUID id) {
  Invitee invitee = repository.findById(id).orElse(null);
  if (invitee != null) {
  repository.delete(invitee);
  }
  return invitee;
  }

  // TODO: Студент должен найти проблемы в этом методе
  @PutMapping("/invitees/{id}/status")
  public Invitee updateStatus(@PathVariable UUID id, @RequestBody Map<String, String> body) {
  try {
  Invitee invitee = repository.findById(id).orElseThrow();
  String status = body.get("status");

           // Бизнес-логика в контроллере
           if (status.equals("ACTIVE") || status.equals("INACTIVE")) {
               invitee.setStatus(status);
           } else {
               throw new RuntimeException("Invalid status");
           }

           return repository.save(invitee);
       } catch (Exception e) {
           // Пустой catch
           return null;
       }
  }
  }

## Code Review Checklist

###  ️Категория 1: API Design (5 проблем)

[ ] 1.1 Неправильные HTTP методы

[ ] 1.2 Неправильные статус коды

[ ] 1.3 Плохой naming: глаголы в URL

[ ] 1.4 Entity вместо DTO в response

[ ] 1.5 Нет пагинации для списков
### Категория 2: Security (5 проблем)
[ ] 2.1 SQL injection через конкатенацию

[ ] 2.2 Exposure внутренних полей

[ ] 2.3 Нет валидации входных данных

[ ] 2.4 Stack trace в error response

[ ] 2.5 Missing authorization checks
### Категория 3: Error Handling (4 проблемы)
[ ] 3.1 Пустые catch блоки

[ ] 3.2 500 на бизнес-ошибки вместо 4xx

[ ] 3.3 Generic error messages без деталей

[ ] 3.4 Нет логирования ошибок
### Категория 4: Code Quality (4 проблемы)
[ ] 4.1 Бизнес-логика в контроллере

[ ] 4.2 Дублирование кода

[ ] 4.3 God Controller: слишком много методов

[ ] 4.4 Hardcoded values

## Code Review Report


                            #
                            # Issue #1: [Название url]


< span class = "error-line" > $ { escapedLine } < /span>
**Приоритет:** MINOR
**Местоположение:** InviteeController.java, строка [24], метод [getInvitees()]

**Что плохо:**
[@PostMapping("/getInvitees"), глагол в url]

**Почему плохо:**
[Не соответствует Rest семантике]

**Как исправить:**
[@PostMapping("/invitees")]

                            #
                            # Issue #2: [POST для вывода лидов]


< span class = "error-line" > $ { escapedLine } < /span>
**Приоритет:** MAJOR
**Местоположение:** InviteeController.java, строка [24], метод [getInvitees()]

**Что плохо:**
[@PostMapping("/getInvitees"), глагол в url]

**Почему плохо:**
[Нарушена семантика HTTP метода.]

**Как исправить:**
[@GetMapping("/getInvitees")]

                            #
                            # Issue #3: [SQL Injection]


< span class = "error-line" > $ { escapedLine } < /span>
**Приоритет:** CRITICAL
**Местоположение:** InviteeController.java, строка [42], метод [create(@RequestBody Map<String, Object> params)]

**Что плохо:**
[        String sql = "SELECT COUNT(*) FROM invitees WHERE email = '" + email + "'";
]

**Почему плохо:**
[SQL Injection, злоумышленник может вставить подставить свой SQL запрос вместо email ]

**Как исправить:**
[Использовать derived method из JpaRepository]

                            #
                            # Issue #4: [Нет DTO]


< span class = "error-line" > $ { escapedLine } < /span>
**Приоритет:** MAJOR
**Местоположение:** InviteeController.java, строка [-], метод [-]

**Что плохо:**
[    Не используется DTO в контроллере
]

**Почему плохо:**
[Мы получаем и передаем лишние поля в запросах]

**Как исправить:**
[Создать record DTO для response и класс DTO для request]

                            #
                            # Issue #5: [Нарушение DRY]


< span class = "error-line" > $ { escapedLine } < /span>
**Приоритет:** MAJOR
**Местоположение:** InviteeController.java, строка [67], метод [updateStatus(@PathVariable UUID id, @RequestBody Map<String, String> body)]

**Что плохо:**
[        try {
Invitee invitee = repository.findById(id).orElseThrow();
String status = body.get("status");

            // Бизнес-логика в контроллере
            if (status.equals("ACTIVE") || status.equals("INACTIVE")) {
                invitee.setStatus(status);
            } else {
                throw new RuntimeException("Invalid status");
            }
]

**Почему плохо:**
[Нарушение DRY, код менее читабелен и мы его дублируем вместо решения с GlobalExceptionHandler]

**Как исправить:**
[Вынести handle всех exceptions в отдельный GlobalExceptionHandler класс]

                            #
                            # Issue #6: [Бизнес-логика в контроллере]


< span class = "error-line" > $ { escapedLine } < /span>
**Приоритет:** MAJOR
**Местоположение:** InviteeController.java, строка [45], метод [create(@RequestBody Map<String, Object> params)]

**Что плохо:**
[        Invitee invitee = new Invitee();
invitee.setId(UUID.randomUUID());
invitee.setEmail(email);
invitee.setFirstName(firstName);
invitee.setCreatedAt(Instant.now());
]

**Почему плохо:**
[Нарушение Single Responsibility Principle, один из SOLID принципов (код менее читаем, менее логичен)]

**Как исправить:**
[Вынести в отдельный Service класс бизнес-логику]

                            #
                            # Issue #7: [Нет ResponseEntity]


< span class = "error-line" > $ { escapedLine } < /span>
**Приоритет:** MAJOR
**Местоположение:** InviteeController.java, строка [X], метод [название]

**Что плохо:**
[Нет ResponseEntity и категоризации HTTP ответов (будут либо 200 ОК , либо 500, либо 404)]

**Почему плохо:**
[Нарушение Rest Api Design, стандартизации для всех]

**Как исправить:**
[Возвращать ResponseEntity и указывать в нем конкретный статус код]

                            #
                            # Issue #8: [Нет логгирования ошибок]


< span class = "error-line" > $ { escapedLine } < /span>
**Приоритет:** MINOR
**Местоположение:** InviteeController.java, строка [X], метод [название]

**Что плохо:**
[Нет логгирования]

**Почему плохо:**
[Debug будет неудобным]

**Как исправить:**
[Добавить @Slf4j аннотацию и сделать категоризированные логи по уровням, Error для internal server error и т.д.]

                            #
                            # Issue #9: [Нет валидации входных данных]


< span class = "error-line" > $ { escapedLine } < /span>
**Приоритет:** MAJOR
**Местоположение:** InviteeController.java, строка [X], метод [название]

**Что плохо:**
[Нет валидации для данных, вводимых пользователем]

**Почему плохо:**
[Может привести к большему количеству ошибок 5xx, которые мы должны были обработать]

**Как исправить:**
[Добавить @NotNull,@NotBlank и т.п. валидирующие аннотации на поля DTO и @Valid около @RequestBody]

                            #
                            # Issue #10: [Нет пагинации для списков]


< span class = "error-line" > $ { escapedLine } < /span>
**Приоритет:** MAJOR
**Местоположение:** InviteeController.java, строка [X], метод [название]

**Что плохо:**
[// Нет пагинации
@PostMapping("/getInvitees")
public List<Invitee> getInvitees() {
return repository.findAll();
}]

**Почему плохо:**
[Может привести к плохой производительности при большом количестве invitees]

**Как исправить:**
[Добавить @PageableDefault(size = ...) Pageable pageable]



## Refactoring Summary

# Refactoring Summary: InviteeController

| Метрика | До рефакторинга | После рефакторинга |
|---------|-----------------|-------------------|
| Строк кода в контроллере | 85 | 108 |
| Количество зависимостей | 1 | 2 |
| Цикломатическая сложность | 9 | 5 |
| Проблем категории CRITICAL | 1 | 0 |
| Проблем категории MAJOR | 7 | 0 |
| Проблем категории MINOR | 2 | 0 |

API Design
✅ Issue #1: [Неправильные HTTP статусы] — [Исп. ResponseEntity]
✅ Issue #2: [Неправильные HTTP url] — [Поменяли endpoints]
Security
✅ Issue #1: [Sql injection] — [Убрали ввод sql через конкатенацию String и использовали derived methods]
✅ Issue #2: [Не было DTO] — [Добавили DTO, теперь "пароль из Entity не украдут"]

Error Handling
✅ Issue #1: [DRY, используются try catch на методах контроллера] — [вынесли это в ответственность GlobalExceptionHandler]
Code Quality
✅ Issue #1: [Бизнес логика в контроллере] — [Вынесли бизнес логику в отдельный service класс]