
## Цель Sprint 7


За 1.5 недели (38 часов) мигрировать CRM с InMemory на PostgreSQL через Spring Data JPA:
1. Setup PostgreSQL + JDBC драйвер — 4 часа
2. Spring Data JPA Repository — 5 часов
3. @Entity маппинг для Lead/Contact/Address — 5 часов
4. @Transactional + self-invocation problem — 5 часов
5. Company сущность (1:N связь) — 5 часов
6. Product сущность (N:M через junction) — 5 часов
7. Демо и GATE-3 — 2 часа
8. Буфер — 7 часов

Результат: Данные CRM сохраняются между запусками, готовность к REST API модулю.
1. Какие сущности есть в доменной модели? (Lead, Contact, Address, Deal)
2. Какой Repository используется сейчас? (InMemoryLeadRepository, InMemoryDealRepository)
3. Что происходит с данными при рестарте? (Теряются полностью)
4. Зависит ли LeadService от типа хранилища? (Нет, работает через интерфейс)


| Риск/Задача | Часы | Урок | Почему столько времени |
| :--- | :--- | :--- | :--- |
| PostgreSQL установка + DBeaver | 4 | BCORE-26 | Первый раз — возможны проблемы с портами, паролями, services |
| Spring Data JPA setup | 5 | BCORE-27 | JpaRepository, spring-boot-starter-data-jpa, application.yml |
| @Entity маппинг Lead/Contact/Address | 5 | BCORE-28 | @Entity, @Id, @GeneratedValue, @Column, @Embedded |
| @Transactional + self-invocation | 5 | BCORE-29 | ACID демонстрация, proxy механизм, rollback |
| Company (1:N связь) | 5 | BCORE-30 | @ManyToOne, @OneToMany(mappedBy), cascade |
| Product (N:M через junction) | 5 | BCORE-31 | DealProduct junction entity, @EmbeddedId |
| GATE-3 демо БД | 2 | GATE-3 | Демо CRM с PostgreSQL, 3 запроса |
| Буфер | 7 | - | Непредвиденные проблемы |
| **ИТОГО** | **38** | **8 уроков** | **1.5 недели интенсивной работы** |



## План миграции по шагам
                            


### Фаза 1: Setup PostgreSQL (BCORE-26) — 4 часа

- День 1: Установка PostgreSQL, создание БД crm, DBeaver подключение
- Проверка: SELECT 1 из DBeaver работает


### Фаза 2: Spring Data JPA Repository (BCORE-27, 28) — 10 часов

- День 2-3: spring-boot-starter-data-jpa, application.yml с datasource
- День 3-4: @Entity Lead с @Id, @GeneratedValue(UUID), @Column
- День 4: @Embedded Contact, Address
- Проверка: ./gradlew bootRun создаёт таблицы в БД


### Фаза 3: @Transactional (BCORE-29) — 5 часов

- День 5: @Transactional на Service методах
- День 5: Self-invocation problem демонстрация и решение
- Проверка: Rollback работает при ошибке


### Фаза 4: Связи между сущностями (BCORE-30, 31) — 10 часов

- День 6-7: Company с @OneToMany → Lead
- День 8-9: Product и DealProduct junction
- Проверка: N:M связь работает через junction


### Финал: GATE-3 — 2 часа

- День 10: Демонстрация работающей CRM с PostgreSQL


## Система отчётности Sprint 7
                            


### #daily статусы (каждый день в статус-треде)

Формат:
- **Что сделано сегодня:** [конкретный урок или задача]
- **JPA прогресс:** [какие сущности замаплены, работает ли]
  < span class = "error-line" > $ { escapedLine } < /span>
- **Следующий шаг:** [завтра делаю...]


### #weekly ретроспектива (каждую субботу)

Формат:
- **Прогресс за неделю:** [какие уроки завершены]
- **Сложности:** [что заняло больше времени чем планировал]
- **JPA инсайты:** [что понял про ORM]
- **Корректировка плана:** [нужно ли больше/меньше времени]


## Контрольные точки Sprint 7
                            


### После BCORE-27 (Spring Data JPA работает)

- [ ] LeadJpaRepository extends JpaRepository<Lead, UUID>
- [ ] spring-boot-starter-data-jpa в build.gradle
- [ ] application.yml с spring.datasource.url
- [ ] ./gradlew bootRun создаёт таблицы


### После BCORE-28 (@Entity маппинг)

- [ ] Lead класс с @Entity, @Id, @GeneratedValue(UUID)
- [ ] Contact как @Embedded внутри Lead
- [ ] Address как @Embedded внутри Contact
- [ ] Все поля с @Column где нужно


### После BCORE-29 (@Transactional работает)

- [ ] @Transactional на LeadService методах
- [ ] Self-invocation problem понял и решил
- [ ] Могу объяснить что такое proxy в Spring


### После BCORE-31 (N:M связь)
                            

- [ ] Company с @OneToMany(mappedBy= "company") List leads
                                

- [ ] Lead с @ManyToOne @JoinColumn(name= "company_id") Company

- [ ] DealProduct junction entity с @EmbeddedId
- [ ] N:M связь Deal↔Product работает


### Финальная проверка (GATE-3)

- [ ] PostgreSQL БД crm с таблицами leads, contacts, deals, company, product
- [ ] Все CRUD операции работают через веб-интерфейс
- [ ] Coverage ≥80% для persistence слоя
- [ ] Могу ответить на вопросы: ACID, @Transactional, self-invocation

