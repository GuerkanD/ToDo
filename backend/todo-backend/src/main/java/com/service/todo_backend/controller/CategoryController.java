package com.service.todo_backend.controller;

import java.util.List;
import java.util.Optional;

import com.service.todo_backend.model.Category;
import com.service.todo_backend.payload.in.TaskDTO;
import com.service.todo_backend.payload.out.CategoryResponseDTO;
import com.service.todo_backend.payload.out.TaskCategoryResponseDTO;
import com.service.todo_backend.service.TaskService;
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
    private final TaskService taskService;

    public CategoryController(CategoryService categoryService, UserService userService, TaskService taskService) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.taskService = taskService;
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

    @PostMapping("/{id}/tasks")
    public ResponseEntity<MessageResponseDTO> createTaskOfCategory (@Valid @RequestBody TaskDTO taskDTO,@PathVariable Long id, Authentication authentication) {
        Optional<User> user = userService.getUserById((Long) authentication.getPrincipal());
        Optional<Category> category = categoryService.getCategoryById(id,user.orElseThrow().getId());
        if (!taskService.checkValidTask(taskDTO.title())) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Invalid task"));
        }
        if (taskService.createTask(taskDTO, category.orElseThrow(), user.orElseThrow())) {
            return ResponseEntity.ok(new MessageResponseDTO("Task created successfully"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponseDTO("Failed to create task"));

    }

    @GetMapping()
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories(Authentication authentication) {
        List<CategoryResponseDTO> categories = categoryService.getAllCategories((Long) authentication.getPrincipal());
        return categories.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(categories)
                : ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<TaskCategoryResponseDTO>> getTasksFromCategory(@PathVariable Long id, Authentication authentication) {
        List<TaskCategoryResponseDTO> tasks = taskService.getTasksOfCategory(id,(Long)authentication.getPrincipal());
        return tasks.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(tasks)
                : ResponseEntity.ok(tasks);
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
        if (categoryService.updateCategory(id, categoryDTO, user.orElseThrow()) == HttpStatus.OK) {
            return ResponseEntity.ok(new MessageResponseDTO("Category updated successfully"));
        }
        else if (categoryService.updateCategory(id, categoryDTO, user.orElseThrow()) == HttpStatus.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponseDTO("Category not found"));
        }
        logger.error("Failed to update category");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponseDTO("Failed to update category"));
    }

}
