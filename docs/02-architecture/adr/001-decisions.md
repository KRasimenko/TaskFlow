# ADR-001: Локальное кэширование SharedPreferences

**Статус:** Принято  
**Контекст:** Требование офлайн-режима на Android.  
**Решение:** Кэш JSON задач в SharedPreferences + синхронизация с REST API при наличии сети.  
**Последствия:** Простая реализация; для масштаба — миграция на Room.

# ADR-002: JWT-аутентификация

**Статус:** Принято  
**Контекст:** Траектория В требует JWT.  
**Решение:** Spring Security + Bearer token, срок 24 ч.  
**Последствия:** Stateless API, масштабируемость.

# ADR-003: Архитектура PCMEF

**Статус:** Принято  
**Решение:** Разделение на Presentation (UI), Control (Services), Entity (DTO/Domain), Foundation (JPA, Security).
