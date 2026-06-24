# TaskFlow — мобильное приложение для планирования задач

**Автор:** Герасименко Константин  
**Группа:** ПИЖ-б-о-23-1  
**Траектория:** В — Мобильная разработка  
**Руководитель:** Свистунов И.В.

Клиент-серверная система: Android-клиент + **Spring Boot REST API** + JWT + PostgreSQL.

---

## Структура репозитория (как myShelf)

```
TaskFlow/
├── taskflow-app/        # Android (Gradle, модуль app/)
│   └── app/             # Kotlin-клиент + Retrofit
├── taskflow-server/     # Spring Boot сервер (Java, Maven)
├── docker/              # Docker Compose + Dockerfile
├── docs/                # Документация по этапам 00–12
├── README.md
└── LICENSE
```

---

## Быстрый старт

### 1. Сервер (обязательно)

```bash
cd taskflow-server
mvn spring-boot:run
```

- API: http://localhost:8080  
- Swagger: http://localhost:8080/swagger-ui.html  
- Health: http://localhost:8080/api/health  

### 2. Docker (PostgreSQL + сервер)

```bash
cd docker
docker-compose up -d
```

### 3. Android-клиент

```bash
cd taskflow-app
./gradlew :app:installDebug
```

Откройте `taskflow-app/` в Android Studio (модуль `:app`).  
Сервер должен быть запущен: `cd taskflow-server && mvn spring-boot:run`  
API с эмулятора: `http://10.0.2.2:8080/`

---

## REST API (11 эндпоинтов)

| Метод | Путь | Описание |
|-------|------|----------|
| GET | `/api/health` | Проверка работы сервера |
| POST | `/api/auth/register` | Регистрация |
| POST | `/api/auth/login` | Вход, JWT |
| GET | `/api/users/me` | Профиль |
| GET | `/api/tasks` | Список задач |
| POST | `/api/tasks` | Создание |
| GET | `/api/tasks/{id}` | Детали |
| PUT | `/api/tasks/{id}` | Обновление |
| DELETE | `/api/tasks/{id}` | Удаление |
| GET | `/api/tasks/starred` | Избранное |
| PATCH | `/api/tasks/{id}/star` | Звезда |

---

## Тестирование сервера

```bash
cd taskflow-server
mvn test
# JaCoCo: target/site/jacoco/index.html
```

Порог покрытия: **≥ 40 %**.

---

## Документация

| Этап | Папка |
|------|-------|
| 0. Инициация | `docs/00-project-charter/` |
| 1. Требования | `docs/01-requirements/` |
| 2. Архитектура | `docs/02-architecture/` |
| 3. БД | `docs/03-database/` |
| 4. Детальное проектирование | `docs/04-detailed-design/` |
| 5. Реализация | `docs/05-implementation/` |
| 6. Тестирование | `docs/06-testing/` |
| 10. Развёртывание | `docs/10-deployment/` |
| 12. Пояснительная записка | `docs/12-final-report/` |


## GitHub

https://github.com/KRasimenko/TaskFlow
