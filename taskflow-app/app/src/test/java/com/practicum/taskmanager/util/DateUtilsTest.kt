package com.practicum.taskmanager.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class DateUtilsTest {

    @Test
    fun todayIso_format() {
        val iso = DateUtils.todayIso()
        assertTrue(iso.matches(Regex("\\d{4}-\\d{2}-\\d{2}")))
    }

    @Test
    fun isSameDay() {
        val a = Calendar.getInstance()
        val b = Calendar.getInstance()
        assertTrue(DateUtils.isSameDay(a, b))
    }

    @Test
    fun calendarDays_has42Cells() {
        val cal = Calendar.getInstance()
        val days = DateUtils.getCalendarDays(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH))
        assertEquals(42, days.size)
    }
}
