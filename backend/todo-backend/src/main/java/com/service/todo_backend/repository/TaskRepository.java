package com.service.todo_backend.repository;

import com.service.todo_backend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByIdAndUserId(Long id, Long userId);
    List<Task> findAllByUserId(Long id);
    List<Task> findAllByCategoryIdAndUserId(Long categoryId, Long userId);
    void deleteTaskByIdAndUserId(Long id, Long userId);
}
