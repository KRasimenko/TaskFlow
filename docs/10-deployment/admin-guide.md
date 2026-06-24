# Руководство администратора TaskFlow

## 1. Требования к серверу

- JDK 17+
- 512 MB RAM минимум
- Порт 8080 (HTTP)

## 2. Установка

```bash
cd backend
./gradlew bootRun
```

## 3. Конфигурация (application.yml)

| Параметр | Описание | По умолчанию |
|----------|----------|--------------|
| `server.port` | Порт API | 8080 |
| `spring.datasource.url` | JDBC URL | jdbc:h2:mem:taskflow |
| `app.jwt.secret` | Секрет JWT | (env JWT_SECRET) |
| `app.jwt.expiration-ms` | Срок токена | 86400000 |

## 4. PostgreSQL (production)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/taskflow
    username: taskflow
    password: ${DB_PASSWORD}
```

Применить DDL: `docs/04-database/schema.sql`

## 5. Мониторинг

- Health: `GET /actuator/health`
- OpenAPI: `GET /swagger-ui.html`

## 6. Резервное копирование

Ежедневный pg_dump таблиц `users`, `tasks`.

## 7. Безопасность

- Сменить JWT_SECRET в production
- HTTPS через reverse proxy (nginx)
- Ограничить CORS доменами клиента
