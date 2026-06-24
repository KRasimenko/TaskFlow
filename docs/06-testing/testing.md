# Этап 6: Качество и тестирование

## Модульные тесты

### Backend (JUnit 5)

```bash
./gradlew :backend:test :backend:jacocoTestReport
```

| Тест | Описание |
|------|----------|
| `JwtTokenProviderTest` | Генерация и валидация JWT |
| `TaskFlowIntegrationTest` | Регистрация, CRUD задач, избранное |

### Android (JUnit 4)

```bash
./gradlew :app:testDebugUnitTest
```

| Тест | Описание |
|------|----------|
| `TaskModelTest` | Enum приоритетов и статусов |
| `DateUtilsTest` | Формат дат, календарная сетка |

## JaCoCo

Порог покрытия: **≥ 40 %** (настроено в `backend/build.gradle.kts`).

Отчёт HTML: `backend/build/reports/jacoco/index.html`

После сборки скопируйте отчёт в эту папку:

```bash
./gradlew :backend:test :backend:jacocoTestReport
cp -r backend/build/reports/jacoco docs/07-quality/jacoco-report/
```

## Статический анализ

- Android Lint — `./gradlew :app:lint`
- Рекомендуется SonarQube для CI

## Чек-лист перед защитой

- [ ] Все тесты backend проходят
- [ ] JaCoCo ≥ 40 %
- [ ] Swagger UI доступен
- [ ] Android unit-тесты проходят
