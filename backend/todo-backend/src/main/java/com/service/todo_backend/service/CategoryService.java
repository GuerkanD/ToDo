package com.service.todo_backend.service;
import java.util.List;
import java.util.Optional;

import com.service.todo_backend.payload.out.CategoryResponseDTO;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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

    public List<CategoryResponseDTO> getAllCategories(Long userId) {
        try {
            return categoryRepository.findAllByUserId(userId)
                    .stream()
                    .map(category -> new CategoryResponseDTO(
                            category.getCategoryId(),
                            category.getTitle(),
                            category.getContent()
                    ))
                    .toList();
        } catch (Exception e) {
            logger.error("Error while fetching the Categories: {}",e.getMessage());
            return List.of();
        }
    }

    @Transactional
    public boolean deleteCategory(Long categoryId, Long userId) {
        try {
            categoryRepository.deleteCategoryByIdAndUserId(categoryId, userId);
            return true;
        } catch (Exception e) {
            logger.error("Error while deleting category: {}", e.getMessage());
            return false;
        }
    }

    public HttpStatus updateCategory(Long categoryId, CategoryDTO categoryDTO, User user) {
        try {
            Optional<Category> category = categoryRepository.findByIdAndUserId(categoryId, user.getId());
            if (category.isPresent()) {
                category.get().setTitle(categoryDTO.title());
                category.get().setContent(categoryDTO.description());
                categoryRepository.save(category.get());
                return HttpStatus.OK;
            }
            return HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            logger.error("Error while updating category: {}", e.getMessage());
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    public boolean checkValidCategory(CategoryDTO categoryDTO) {
        if (categoryDTO.title().isEmpty()) {
            return true;
        }
        return categoryDTO.title().length() > 100;
    }

    public Optional<Category> getCategoryById(Long categoryId, Long userId){
        return categoryRepository.findByIdAndUserId(categoryId,userId);
    }
}
