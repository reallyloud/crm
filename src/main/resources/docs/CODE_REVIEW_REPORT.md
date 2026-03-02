
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

