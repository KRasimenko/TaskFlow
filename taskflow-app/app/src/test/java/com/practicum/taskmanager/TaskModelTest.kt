package com.practicum.taskmanager

import com.practicum.taskmanager.data.TaskPriority
import com.practicum.taskmanager.data.TaskStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class TaskModelTest {

    @Test
    fun taskPriorityValues() {
        assertEquals(3, TaskPriority.entries.size)
        assertEquals(TaskPriority.HIGH, TaskPriority.valueOf("HIGH"))
    }

    @Test
    fun taskStatusValues() {
        assertEquals(TaskStatus.DONE, TaskStatus.valueOf("DONE"))
    }
}
