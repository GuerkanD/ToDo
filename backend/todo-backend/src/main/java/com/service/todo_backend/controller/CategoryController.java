package com.service.todo_backend.controller;

import java.util.List;
import java.util.Optional;

import com.service.todo_backend.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.service.todo_backend.model.User;
import com.service.todo_backend.payload.in.CategoryDTO;
import com.service.todo_backend.payload.out.MessageResponseDTO;
import com.service.todo_backend.service.CategoryService;
import com.service.todo_backend.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;
    private final UserService userService;

    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<MessageResponseDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, Authentication authentication) {
        Optional<User> user = userService.getUserById((Long) authentication.getPrincipal());
        if (categoryService.checkValidCategory(categoryDTO)) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Invalid category"));
        }
        if (categoryService.createCategory(categoryDTO, user.orElseThrow())) {
            return ResponseEntity.ok(new MessageResponseDTO("Category created successfully"));
        }
        logger.error("Failed to create category");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponseDTO("Failed to create category"));
    }

    @GetMapping()
    public ResponseEntity<List<Category>> getAllCategories(Authentication authentication) {
        List<Category> categories = categoryService.getAllCategories((Long) authentication.getPrincipal());
        return categories.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(categories)
                : ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id, Authentication authentication) {
        Optional<Category> category = categoryService.getCategoryById(id, (Long) authentication.getPrincipal());
        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteCategory(@PathVariable Long id, Authentication authentication) {
        Optional<User> user = userService.getUserById((Long) authentication.getPrincipal());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponseDTO("Unauthorized"));
        }
        if (categoryService.deleteCategory(id, user.get().getId())) {
            return ResponseEntity.ok(new MessageResponseDTO("Category deleted successfully"));
        }
        logger.error("Failed to delete category");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponseDTO("Failed to delete category"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO, Authentication authentication) {
        Optional<User> user = userService.getUserById((Long) authentication.getPrincipal());
        if (categoryService.checkValidCategory(categoryDTO)) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Invalid category"));
        }
        if (categoryService.updateCategory(id, categoryDTO, user.orElseThrow())) {
            return ResponseEntity.ok(new MessageResponseDTO("Category updated successfully"));
        }
        logger.error("Failed to update category");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponseDTO("Failed to update category"));
    }

}
