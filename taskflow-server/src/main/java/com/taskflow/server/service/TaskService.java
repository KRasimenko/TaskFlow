package com.taskflow.server.service;

import com.taskflow.server.dto.TaskDto;
import com.taskflow.server.dto.TaskRequest;
import com.taskflow.server.entity.TaskEntity;
import com.taskflow.server.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskDto> findAll(Long userId) {
        return taskRepository.findByUserIdOrderByIdDesc(userId).stream().map(this::toDto).toList();
    }

    public List<TaskDto> findStarred(Long userId) {
        return taskRepository.findByUserIdAndStarredTrueOrderByIdDesc(userId).stream().map(this::toDto).toList();
    }

    public TaskDto findById(Long userId, Long taskId) {
        return toDto(getOwned(userId, taskId));
    }

    public TaskDto create(Long userId, TaskRequest request) {
        TaskEntity entity = mapRequest(new TaskEntity(), request);
        entity.setUserId(userId);
        return toDto(taskRepository.save(entity));
    }

    public TaskDto update(Long userId, Long taskId, TaskRequest request) {
        TaskEntity entity = getOwned(userId, taskId);
        mapRequest(entity, request);
        return toDto(taskRepository.save(entity));
    }

    public void delete(Long userId, Long taskId) {
        TaskEntity entity = getOwned(userId, taskId);
        taskRepository.delete(entity);
    }

    public TaskDto toggleStar(Long userId, Long taskId) {
        TaskEntity entity = getOwned(userId, taskId);
        entity.setStarred(!entity.isStarred());
        return toDto(taskRepository.save(entity));
    }

    private TaskEntity getOwned(Long userId, Long taskId) {
        return taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Задача не найдена"));
    }

    private TaskEntity mapRequest(TaskEntity entity, TaskRequest request) {
        entity.setTitle(request.title());
        entity.setDescription(request.description());
        if (request.category() != null) entity.setCategory(request.category());
        if (request.priority() != null) entity.setPriority(request.priority());
        if (request.status() != null) entity.setStatus(request.status());
        entity.setDueDate(request.dueDate());
        if (request.starred() != null) entity.setStarred(request.starred());
        return entity;
    }

    private TaskDto toDto(TaskEntity e) {
        return new TaskDto(e.getId(), e.getTitle(), e.getDescription(), e.getCategory(),
                e.getPriority(), e.getStatus(), e.getDueDate(), e.isStarred(), e.getCreatedAt());
    }
}
