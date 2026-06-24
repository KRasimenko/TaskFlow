package com.taskflow.server;

import com.taskflow.server.dto.LoginRequest;
import com.taskflow.server.dto.RegisterRequest;
import com.taskflow.server.dto.TaskRequest;
import com.taskflow.server.service.AuthService;
import com.taskflow.server.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TaskFlowIntegrationTest {

    @Autowired AuthService authService;
    @Autowired TaskService taskService;

    Long userId;

    @BeforeEach
    void setUp() {
        var auth = authService.register(new RegisterRequest("Константин", "test@taskflow.app", "password123"));
        userId = auth.user().id();
    }

    @Test
    void registerAndLogin() {
        var login = authService.login(new LoginRequest("test@taskflow.app", "password123"));
        assertNotNull(login.token());
        assertEquals("Константин", login.user().name());
    }

    @Test
    void crudTask() {
        var created = taskService.create(userId, new TaskRequest(
                "Тест", "Описание", "Работа", "HIGH", "TODO",
                LocalDate.now(), false));
        assertEquals("Тест", created.title());

        var list = taskService.findAll(userId);
        assertEquals(1, list.size());

        var updated = taskService.update(userId, created.id(), new TaskRequest(
                "Обновлено", "Новое", "Личное", "LOW", "DONE",
                LocalDate.now(), true));
        assertEquals("DONE", updated.status());

        taskService.delete(userId, created.id());
        assertTrue(taskService.findAll(userId).isEmpty());
    }

    @Test
    void toggleStar() {
        var task = taskService.create(userId, new TaskRequest(
                "Star", null, "Работа", "MEDIUM", "TODO", LocalDate.now(), false));
        var starred = taskService.toggleStar(userId, task.id());
        assertTrue(starred.starred());
        assertEquals(1, taskService.findStarred(userId).size());
    }
}
