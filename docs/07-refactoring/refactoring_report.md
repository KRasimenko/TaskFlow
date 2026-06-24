# Отчёт о рефакторинге

1. Выделен слой `data/remote` (Retrofit, JWT) в taskflow-app.
2. DTO отделены от доменных моделей через `TaskMapper`.
3. На сервере бизнес-логика сосредоточена в `AuthService` и `TaskService`.
4. `GlobalExceptionHandler` унифицирует ошибки REST API.
