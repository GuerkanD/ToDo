package com.service.todo_backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.service.todo_backend.model.Category;
import com.service.todo_backend.model.User;
import com.service.todo_backend.payload.in.CategoryDTO;
import com.service.todo_backend.repository.CategoryRepository;
import com.service.todo_backend.repository.UserRepository;

import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    
    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public Category createCategory(CategoryDTO categoryDTO, Long userId) { //TODO add proper validation
        try {
            Optional<User> user = userRepository.findById(userId);
            if (user.isEmpty()) {
                logger.error("User not found");
                return null;
            }
            return categoryRepository.save(new Category(categoryDTO.title(), categoryDTO.description(), user.get()));
        } catch (Exception e) {
            logger.error("Error while creating category: {}", e.getMessage());
            return null;
        }
    }


}
