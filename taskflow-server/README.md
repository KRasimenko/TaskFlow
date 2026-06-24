# taskflow-server

Spring Boot REST API для TaskFlow.

## Запуск

```bash
mvn spring-boot:run
```

Профили: `dev` (H2, по умолчанию), `prod` (PostgreSQL).

## Проверка

- http://localhost:8080/api/health
- http://localhost:8080/swagger-ui.html

## Тесты

```bash
mvn test
open target/site/jacoco/index.html
```

## Структура (PCMEF)

| Пакет | Слой |
|-------|------|
| controller | Control (Facade) |
| service | Mediator |
| entity | Entity |
| repository | Foundation |
| dto | Presentation (DTO) |
| security | JWT |
