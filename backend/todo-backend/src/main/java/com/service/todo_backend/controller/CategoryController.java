package com.service.todo_backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.service.todo_backend.model.Category;
import com.service.todo_backend.payload.in.CategoryDTO;
import com.service.todo_backend.payload.out.MessageResponseDTO;
import com.service.todo_backend.service.CategoryService;

import jakarta.validation.Valid;

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

    @PostMapping()
    public ResponseEntity<MessageResponseDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, Authentication authentication) {
        if (categoryDTO.title().isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error: Title cannot be empty!"));
        }
        if (categoryDTO.title().length() > 100) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error: Title must be at most 100 characters long!"));
        }


        Category category = categoryService.createCategory(categoryDTO, (Long)authentication.getPrincipal());
        if (category != null) {
            return ResponseEntity.ok(new MessageResponseDTO("Category created successfully"));
        } else {
            logger.error("Failed to create category");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponseDTO("Failed to create category"));
        }
    }
    

}
