package com.service.todo_backend.service;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.service.todo_backend.model.Category;
import com.service.todo_backend.model.User;
import com.service.todo_backend.payload.in.CategoryDTO;
import com.service.todo_backend.repository.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public boolean createCategory(CategoryDTO categoryDTO, User user) {
        try {
            categoryRepository.save(new Category(categoryDTO.title(), categoryDTO.description(), user));
            return true;
        } catch (Exception e) {
            logger.error("Error while creating category: {}", e.getMessage());
            return false;
        }
    }

    public List<Category> getAllCategories(Long userId) {
        return categoryRepository.findAllByUserId(userId);
    }

    public boolean deleteCategory(Long categoryId, Long userId) {
        try {
            categoryRepository.deleteCategoryByCategoryIdAndUserId(categoryId, userId);
            return true;
        } catch (Exception e) {
            logger.error("Error while deleting category: {}", e.getMessage());
            return false;
        }
    }

}
