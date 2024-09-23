package com.service.todo_backend.repository;

import com.service.todo_backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findAllByUserId(Long userId);
    Optional<Category> findByIdAndUserId(Long id, Long userId);
}
