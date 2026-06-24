package com.taskflow.server.controller;

import com.taskflow.server.dto.TaskDto;
import com.taskflow.server.dto.TaskRequest;
import com.taskflow.server.dto.UserDto;
import com.taskflow.server.service.AuthService;
import com.taskflow.server.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;
    private final AuthService authService;

    public TaskController(TaskService taskService, AuthService authService) {
        this.taskService = taskService;
        this.authService = authService;
    }

    @GetMapping("/users/me")
    public UserDto me(Authentication auth) {
        return authService.getUser(userId(auth));
    }

    @GetMapping("/tasks")
    public List<TaskDto> list(Authentication auth) {
        return taskService.findAll(userId(auth));
    }

    @GetMapping("/tasks/starred")
    public List<TaskDto> starred(Authentication auth) {
        return taskService.findStarred(userId(auth));
    }

    @GetMapping("/tasks/{id}")
    public TaskDto get(@PathVariable Long id, Authentication auth) {
        return taskService.findById(userId(auth), id);
    }

    @PostMapping("/tasks")
    public TaskDto create(@Valid @RequestBody TaskRequest request, Authentication auth) {
        return taskService.create(userId(auth), request);
    }

    @PutMapping("/tasks/{id}")
    public TaskDto update(@PathVariable Long id, @Valid @RequestBody TaskRequest request, Authentication auth) {
        return taskService.update(userId(auth), id, request);
    }

    @DeleteMapping("/tasks/{id}")
    public void delete(@PathVariable Long id, Authentication auth) {
        taskService.delete(userId(auth), id);
    }

    @PatchMapping("/tasks/{id}/star")
    public TaskDto toggleStar(@PathVariable Long id, Authentication auth) {
        return taskService.toggleStar(userId(auth), id);
    }

    private Long userId(Authentication auth) {
        return (Long) auth.getPrincipal();
    }
}
