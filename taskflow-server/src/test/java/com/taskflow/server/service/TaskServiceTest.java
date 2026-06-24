package com.taskflow.server.service;

import com.taskflow.server.dto.TaskRequest;
import com.taskflow.server.entity.TaskEntity;
import com.taskflow.server.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock TaskRepository taskRepository;
    @InjectMocks TaskService taskService;

    @Test
    void createTask() {
        when(taskRepository.save(any(TaskEntity.class))).thenAnswer(inv -> {
            TaskEntity e = inv.getArgument(0);
            e.setId(10L);
            return e;
        });
        var dto = taskService.create(1L, new TaskRequest(
                "Задача", "Описание", "Работа", "HIGH", "TODO", LocalDate.now(), false));
        assertEquals("Задача", dto.title());
        assertEquals(10L, dto.id());
    }

    @Test
    void findById_notFound() {
        when(taskRepository.findByIdAndUserId(5L, 1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> taskService.findById(1L, 5L));
    }

    @Test
    void toggleStar() {
        TaskEntity entity = new TaskEntity();
        entity.setId(1L);
        entity.setUserId(1L);
        entity.setTitle("T");
        entity.setDueDate(LocalDate.now());
        entity.setStarred(false);
        when(taskRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(entity));
        when(taskRepository.save(entity)).thenReturn(entity);

        var result = taskService.toggleStar(1L, 1L);
        assertTrue(result.starred());
    }
}
