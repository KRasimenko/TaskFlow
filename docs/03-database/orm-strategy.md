# ORM-стратегия

## Выбор: Spring Data JPA (Hibernate)

- **Entity** классы `User`, `Task` аннотированы `@Entity`
- **Repository** интерфейсы extends `JpaRepository`
- **DDL:** `spring.jpa.hibernate.ddl-auto=update` (dev), `validate` (prod)
- **Миграции:** скрипт `docs/04-database/schema.sql` для этalon-схемы

## Data Mapper

JPA Entity ↔ DTO через MapStruct/ручной mapper в Service layer — отделение доменной модели от persistence.

## Identity Map

Кэш первого уровня Hibernate в рамках транзакции; на клиенте — singleton TaskRepository.

## Связи

- User 1 — * Task (FK user_id, ON DELETE CASCADE)
