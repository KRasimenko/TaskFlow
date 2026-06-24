# Паттерны рефакторинга TaskFlow

- **Data Mapper** — `TaskMapper` (Android), DTO ↔ Entity (Spring).
- **Identity Map** — Hibernate Persistence Context в транзакции `@Transactional`.
- **Repository** — `TaskRepository` (клиент), `TaskRepository` JPA (сервер).
