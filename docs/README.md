# Курсовой проект TaskFlow

## Файлы

| Файл | Описание |
|------|----------|
| `Kursovoy_TaskFlow.docx` | Готовая пояснительная записка (~60 стр.) |
| `generate_coursework.py` | Скрипт пересборки Word-документа |
| `plantuml/*.puml` | UML-диаграммы (PlantUML) |
| `images/*.png` | Экспортированные диаграммы |

## Диаграммы PlantUML

- `use_case.puml` — диаграмма прецедентов
- `user_stories.puml` — карта User Stories (mindmap)
- `class_diagram.puml` — классы
- `sequence_create_task.puml` — последовательность (создание задачи)
- `activity_auth.puml` — деятельность (вход)
- `navigation.puml` — навигация экранов
- `component.puml` — компоненты
- `gantt.puml` — план разработки

Просмотр: [plantuml.com](https://www.plantuml.com/plantuml/uml/) или плагин PlantUML в IDE.

## Пересборка Word

```bash
pip install python-docx plantuml requests
python docs/generate_coursework.py
```

## Перед сдачей

1. Откройте `Kursovoy_TaskFlow.docx` в Word.
2. Заполните название вуза, ФИО, группу на титульном листе.
3. **Ссылки → Оглавление → Автособираемое** (обновить номера страниц).
4. Проверьте, что объём около 60 страниц (шрифт Times New Roman 14, интервал 1,5).
