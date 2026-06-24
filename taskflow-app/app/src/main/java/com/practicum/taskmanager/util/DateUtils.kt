package com.practicum.taskmanager.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    private val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val ruFormat = SimpleDateFormat("d MMM yyyy", Locale("ru"))

    private val monthNames = arrayOf(
        "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
        "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь",
    )

    val weekdays = arrayOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")

    fun todayIso(): String = isoFormat.format(Date())

    fun formatDateRu(iso: String): String {
        return try {
            val date = isoFormat.parse(iso) ?: return iso
            ruFormat.format(date)
        } catch (_: Exception) {
            iso
        }
    }

    fun monthLabel(year: Int, month: Int): String = "${monthNames[month]} $year"

    fun toIsoDate(calendar: Calendar): String = isoFormat.format(calendar.time)

    fun parseIso(iso: String): Calendar {
        val cal = Calendar.getInstance()
        cal.time = isoFormat.parse(iso) ?: Date()
        return cal
    }

    fun isSameDay(a: Calendar, b: Calendar): Boolean =
        a.get(Calendar.YEAR) == b.get(Calendar.YEAR) &&
            a.get(Calendar.DAY_OF_YEAR) == b.get(Calendar.DAY_OF_YEAR)

    fun isToday(cal: Calendar): Boolean = isSameDay(cal, Calendar.getInstance())

    data class CalendarDay(val date: Calendar, val inMonth: Boolean)

    fun getCalendarDays(year: Int, month: Int): List<CalendarDay> {
        val first = Calendar.getInstance().apply {
            set(year, month, 1, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOffset = (first.get(Calendar.DAY_OF_WEEK) + 5) % 7
        val start = first.clone() as Calendar
        start.add(Calendar.DAY_OF_MONTH, -startOffset)

        return (0 until 42).map { i ->
            val day = start.clone() as Calendar
            day.add(Calendar.DAY_OF_MONTH, i)
            CalendarDay(day, day.get(Calendar.MONTH) == month)
        }
    }

    fun formatSelectedDayRu(cal: Calendar): String {
        val fmt = SimpleDateFormat("EEEE, d MMMM", Locale("ru"))
        return fmt.format(cal.time).replaceFirstChar { it.uppercase() }
    }
}
