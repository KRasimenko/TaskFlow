# Этап 6 — Тестирование

## Запуск

```bash
cd taskflow-server
mvn test
```

## JaCoCo

Отчёт: `taskflow-server/target/site/jacoco/index.html`  
Порог в `pom.xml`: **≥ 40 %**.

## Сводные изображения для пояснительной записки

| Файл | Описание |
|------|----------|
| [jacoco-summary.png](jacoco-summary.png) | Сводка покрытия (Приложение В) |
| [mvn-test-summary.png](mvn-test-summary.png) | Вывод mvn test (Приложение В) |

```bash
python docs/scripts/generate_appendix_images.py
```

## Тестовые классы

- `AuthServiceTest`, `TaskServiceTest`, `JwtTokenProviderTest`
- `TaskFlowIntegrationTest`, `AuthControllerMvcTest`
- Android: `TaskModelTest`, `DateUtilsTest`
