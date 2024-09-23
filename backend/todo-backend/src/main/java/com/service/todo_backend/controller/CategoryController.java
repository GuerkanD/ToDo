package com.service.todo_backend.controller;

import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.service.todo_backend.model.Category;
import com.service.todo_backend.payload.in.CategoryDTO;
import com.service.todo_backend.payload.out.MessageResponseDTO;
import com.service.todo_backend.service.CategoryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping() //TODO Check lengths of Title and Content
    public ResponseEntity<MessageResponseDTO> createCategory(@RequestBody CategoryDTO categoryDTO, Authentication authentication) {
        logger.info("Creating category");
        Category category = categoryService.createCategory(categoryDTO, (Long)authentication.getPrincipal());
        if (category != null) {
            return ResponseEntity.ok(new MessageResponseDTO("Category created successfully"));
        } else {
            logger.error("Failed to create category");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponseDTO("Failed to create category"));
        }
    }
    

}
