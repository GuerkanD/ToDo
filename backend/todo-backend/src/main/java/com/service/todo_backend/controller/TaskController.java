package com.service.todo_backend.controller;

import com.service.todo_backend.model.Task;
import com.service.todo_backend.model.User;
import com.service.todo_backend.payload.in.TaskDTO;
import com.service.todo_backend.payload.out.MessageResponseDTO;
import com.service.todo_backend.service.TaskService;
import com.service.todo_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;


    public TaskController(TaskService taskService,UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<MessageResponseDTO> createTask(@Valid @RequestBody TaskDTO taskDTO, Authentication authentication) {
        Optional<User> user = userService.getUserById((Long) authentication.getPrincipal());
        if(taskService.checkValidTask(taskDTO)) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Invalid task"));
        }
        if (taskService.createTask(taskDTO, user.orElseThrow())) {
            return ResponseEntity.ok(new MessageResponseDTO("Task created successfully"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponseDTO("Failed to create task"));
    }

    @GetMapping()
    public ResponseEntity<List<Task>> getAllTasks(Authentication authentication) {
        List<Task> tasks = taskService.getAllTasks((Long)authentication.getPrincipal());
        return tasks.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(tasks)
                : ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO, Authentication authentication){
        Optional<User> user = userService.getUserById((Long) authentication.getPrincipal());
        if(taskService.checkValidTask(taskDTO)) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Invalid task"));
        }
        if(taskService.updateTasks(id,taskDTO,user.orElseThrow())){
            return ResponseEntity.ok(new MessageResponseDTO("Task updated successfully"));
        }
        return ResponseEntity.internalServerError().body(new MessageResponseDTO("Failed to Update Task"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteTask(@PathVariable Long id, Authentication authentication){
        Optional<User> user = userService.getUserById((Long) authentication.getPrincipal());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponseDTO("Unauthorized"));
        }
        if (taskService.deleteTask(id,user.get().getId())){
            return ResponseEntity.ok(new MessageResponseDTO("Task deleted successfully"));
        }
        return ResponseEntity.internalServerError().body(new MessageResponseDTO("Failed to delete Task"));
    }
}
