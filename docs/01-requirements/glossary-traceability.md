# Глоссарий системы (20+ терминов)

| № | Термин | Определение |
|---|--------|-------------|
| 1 | API | Application Programming Interface |
| 2 | JWT | Токен авторизации в формате JSON Web Token |
| 3 | Entity | JPA-сущность, отображаемая на таблицу БД |
| 4 | DTO | Data Transfer Object для REST |
| 5 | Fragment | UI-компонент Android |
| 6 | ViewBinding | Типобезопасный доступ к layout |
| 7 | NavGraph | Граф навигации Android |
| 8 | Repository | Паттерн доступа к данным |
| 9 | Mediator | Слой PCMEF для координации |
| 10 | Foundation | Инфраструктурный слой PCMEF |
| 11 | H2 | In-memory СУБД для разработки |
| 12 | JPA | Java Persistence API |
| 13 | BCrypt | Алгоритм хеширования паролей |
| 14 | OpenAPI | Спецификация REST API |
| 15 | JaCoCo | Инструмент отчёта покрытия тестами |
| 16 | FAB | Floating Action Button |
| 17 | Chip | UI-элемент фильтра Material |
| 18 | SeedData | Начальные демо-данные |
| 19 | TaskRepository | Класс бизнес-логики задач |
| 20 | AuthService | Сервис аутентификации сервера |
| 21 | CORS | Cross-Origin Resource Sharing |
| 22 | DDL | Data Definition Language — скрипты схемы Б |

## Трассировка требований

| Бизнес-требование | Системное | Артефакт |
|-------------------|-----------|----------|
| Планировать задачи | FR-02 CRUD | TaskController |
| Вход в систему | FR-01 Auth | AuthController, JWT |
| Календарь | FR-04 | ScheduleFragment |
| Избранное | FR-05 | GET /api/tasks/starred |
| Офлайн | NFR-02 | SharedPreferences |
