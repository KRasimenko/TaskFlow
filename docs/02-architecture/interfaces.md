# Спецификация интерфейсов между слоями PCMEF

## Presentation → Control

```kotlin
interface TaskMediator {
    fun loadTasks(): List<TaskDto>
    fun createTask(request: CreateTaskRequest): TaskDto
    fun syncWithServer()
}
```

## Control → Entity / Foundation

```java
public interface TaskService {
    List<TaskDto> findAllForUser(Long userId);
    TaskDto create(Long userId, CreateTaskRequest req);
    void delete(Long userId, Long taskId);
}
```

## REST API (Foundation boundary)

| Метод | Путь | Описание |
|-------|------|----------|
| POST | /api/auth/register | Регистрация |
| POST | /api/auth/login | JWT login |
| GET | /api/tasks | Список задач |
| POST | /api/tasks | Создание |
| GET | /api/tasks/{id} | Одна задача |
| PUT | /api/tasks/{id} | Обновление |
| DELETE | /api/tasks/{id} | Удаление |
| GET | /api/tasks/starred | Избранные |
| PATCH | /api/tasks/{id}/star | Звёздочка |
| GET | /api/users/me | Профиль |

## ADR

См. каталог `docs/03-architecture/adr/`.
