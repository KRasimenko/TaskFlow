# Этап 5: Реализация

## Структура модулей (как myShelf)

```
TaskFlow/
├── taskflow-app/
│   └── app/              # Android-клиент (Kotlin, Retrofit)
├── taskflow-server/      # Spring Boot (Java, Maven)
├── docker/
└── docs/
```

## Android (`taskflow-app/app/`)

| Пакет | Назначение |
|-------|------------|
| `ui/` | Fragment-экраны, адаптеры |
| `data/` | Task, TaskRepository |
| `data/remote/` | Retrofit, JWT, DTO |
| `data/mapper/` | TaskMapper (Data Mapper) |
| `util/` | DateUtils, ApiConfig |

Клиент: **Retrofit** → REST API; локальный кэш **SharedPreferences** + Gson.

## Сервер (`taskflow-server/`)

| Пакет | Слой PCMEF |
|-------|------------|
| `entity` | Entity |
| `repository` | Foundation (JPA) |
| `service` | Mediator |
| `controller` | Control (REST) |
| `dto` | Presentation |
| `security` | JWT, JwtAuthFilter |

## REST API

Swagger UI: `/swagger-ui.html`  
Спецификация: [../09-api/README.md](../09-api/README.md)
