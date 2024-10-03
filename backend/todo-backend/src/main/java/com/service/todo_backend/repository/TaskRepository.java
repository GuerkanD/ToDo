package com.service.todo_backend.repository;

import com.service.todo_backend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUserId(Long id);
    Optional<Task> findByIdAndUserId(Long id, Long userId);
    void deleteTaskByIdAndUserId(Long id, Long userId);
}
