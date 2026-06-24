# Этап 4: Детальное проектирование

## Диаграммы последовательности

| Сценарий | Файл |
|----------|------|
| Вход в систему | [sequence_login.puml](../plantuml/sequence_login.puml) |
| Создание задачи | [sequence_create_task.puml](../plantuml/sequence_create_task.puml) |
| Удаление задачи | [sequence_delete_task.puml](../plantuml/sequence_delete_task.puml) |

## Диаграмма классов проектирования

- [class_diagram.puml](../plantuml/class_diagram.puml)

## Диаграмма навигации

- [navigation.puml](../plantuml/navigation.puml)

## Диаграмма активности

- [activity_auth.puml](../plantuml/activity_auth.puml)

## Спецификация ключевых методов

### AuthService
- `AuthResponse register(RegisterRequest)` — регистрация пользователя
- `AuthResponse login(LoginRequest)` — выдача JWT

### TaskService
- `TaskDto create(Long userId, TaskRequest)` — создание задачи
- `TaskDto update(Long userId, Long id, TaskRequest)` — обновление
- `void delete(Long userId, Long id)` — удаление
- `TaskDto toggleStar(Long userId, Long id)` — избранное
