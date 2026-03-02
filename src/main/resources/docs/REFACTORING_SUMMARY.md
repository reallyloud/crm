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