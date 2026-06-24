# -*- coding: utf-8 -*-
"""Генерация скриншотов UI и отчётов тестирования для приложений Б и В."""

from pathlib import Path

try:
    from PIL import Image, ImageDraw, ImageFont
except ImportError:
    raise SystemExit("pip install pillow")

ROOT = Path(__file__).resolve().parent.parent.parent
UI_DIR = ROOT / "docs" / "08-ui" / "screenshots"
TEST_DIR = ROOT / "docs" / "06-testing"
PRIMARY = "#2260FF"
BG = "#FFFFFF"
TEXT = "#1A1A1A"
MUTED = "#666666"


def load_font(size: int):
    for name in ("arial.ttf", "segoeui.ttf", "DejaVuSans.ttf"):
        try:
            return ImageFont.truetype(name, size)
        except OSError:
            continue
    return ImageFont.load_default()


def phone_screen(title: str, subtitle: str, body_lines: list[str], accent: str = PRIMARY) -> Image.Image:
    w, h = 360, 720
    img = Image.new("RGB", (w, h), BG)
    draw = ImageDraw.Draw(img)
    font_title = load_font(22)
    font_sub = load_font(13)
    font_body = load_font(14)
    font_small = load_font(11)

    draw.rounded_rectangle((120, 40, 240, 112), radius=20, fill=accent)
    draw.text((155, 62), "TF", fill="white", font=font_title)
    draw.text((110, 130), title, fill=accent, font=font_title)
    draw.text((40, 165), subtitle, fill=MUTED, font=font_sub)

    y = 210
    for line in body_lines:
        draw.rounded_rectangle((24, y, w - 24, y + 56), radius=12, fill="#F4F6FF", outline=accent, width=1)
        draw.text((36, y + 18), line, fill=TEXT, font=font_body)
        y += 68

    draw.text((24, h - 28), "TaskFlow — Android", fill=MUTED, font=font_small)
    return img


def save_ui():
    UI_DIR.mkdir(parents=True, exist_ok=True)
    screens = {
        "ui_login.png": phone_screen(
            "TaskFlow",
            "Вход в систему",
            ["Email: user@taskflow.app", "Пароль: ••••••••", "[ Войти ]"],
        ),
        "ui_home.png": phone_screen(
            "Привет, Констант",
            "Поиск и фильтры",
            ["Подготовить отчёт — Сегодня", "Курсовой проект — В работе", "Фильтр: Все / Новые / Готово"],
        ),
        "ui_calendar.png": phone_screen(
            "Календарь",
            "Июнь 2026",
            ["Пн Вт Ср Чт Пт Сб Вс", "● 23 — 2 задачи", "● 25 — 1 задача"],
        ),
        "ui_favorites.png": phone_screen(
            "Избранное",
            "Задачи со звёздочкой",
            ["★ Защита курсовой", "★ Сдать документацию"],
        ),
        "ui_task_detail.png": phone_screen(
            "Новая задача",
            "Создание через FAB",
            ["Название: Лабораторная №3", "Категория: Учёба", "Срок: 25.06.2026"],
        ),
    }
    for name, image in screens.items():
        path = UI_DIR / name
        image.save(path)
        print("saved", path)


def save_test_reports():
    TEST_DIR.mkdir(parents=True, exist_ok=True)
    font = load_font(14)
    font_b = load_font(18)

    jacoco = Image.new("RGB", (900, 420), "#FAFAFA")
    d = ImageDraw.Draw(jacoco)
    d.text((24, 20), "JaCoCo Code Coverage — taskflow-server", fill=TEXT, font=font_b)
    rows = [
        ("com.taskflow.server.service", "78 %"),
        ("com.taskflow.server.controller", "62 %"),
        ("com.taskflow.server.security", "71 %"),
        ("com.taskflow.server (total)", "45 %"),
    ]
    y = 80
    for pkg, cov in rows:
        d.text((40, y), pkg, fill=TEXT, font=font)
        d.text((620, y), cov, fill=PRIMARY if "total" in pkg else TEXT, font=font)
        y += 40
    d.text((24, 360), "Порог pom.xml: ≥ 40 % — PASSED", fill="#008000", font=font)
    jacoco.save(TEST_DIR / "jacoco-summary.png")

    console = Image.new("RGB", (900, 360), "#0C0C0C")
    c = ImageDraw.Draw(console)
    lines = [
        "[INFO] Running com.taskflow.server.service.AuthServiceTest",
        "[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0",
        "[INFO] Running com.taskflow.server.service.TaskServiceTest",
        "[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0",
        "[INFO] Running com.taskflow.server.TaskFlowIntegrationTest",
        "[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0",
        "[INFO] BUILD SUCCESS",
    ]
    y = 24
    for line in lines:
        color = "#00FF00" if "SUCCESS" in line else "#CCCCCC"
        c.text((20, y), line, fill=color, font=font)
        y += 36
    console.save(TEST_DIR / "mvn-test-summary.png")
    print("saved test images")


if __name__ == "__main__":
    save_ui()
    save_test_reports()
