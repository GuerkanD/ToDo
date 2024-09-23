package com.service.todo_backend.repository;

import com.service.todo_backend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findAllByCategoryId(Long categoryId);
    Optional<Task> findByUserId(Long id);
    Optional<Task> findByUserIdAndCategoryId(Long id, Long categoryId);
    Optional<Task> findByIdAndCategoryId(Long id, Long categoryId);
}
