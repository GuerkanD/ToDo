package com.service.todo_backend.repository;

import com.service.todo_backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUserId(Long userId);
    Optional<Category> findByIdAndUserId(Long id, Long userId);
    boolean deleteCategoryByCategoryIdAndUserId(Long categoryId, Long userId);
}
