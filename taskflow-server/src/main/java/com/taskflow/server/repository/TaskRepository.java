package com.taskflow.server.repository;

import com.taskflow.server.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByUserIdOrderByIdDesc(Long userId);
    List<TaskEntity> findByUserIdAndStarredTrueOrderByIdDesc(Long userId);
    Optional<TaskEntity> findByIdAndUserId(Long id, Long userId);
}
