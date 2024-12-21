package com.service.todo_backend.service;

import com.service.todo_backend.model.Category;
import com.service.todo_backend.model.Task;
import com.service.todo_backend.model.User;
import com.service.todo_backend.payload.in.TaskDTO;
import com.service.todo_backend.payload.out.TaskCategoryResponseDTO;
import com.service.todo_backend.payload.out.TaskResponseDTO;
import com.service.todo_backend.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final Logger logger = LoggerFactory.getLogger(TaskService.class);

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public boolean createTask(TaskDTO taskDTO, User user) {
        try {
            taskRepository.save(new Task(taskDTO.title(), taskDTO.content(), user));
            return true;
        } catch (Exception e) {
            logger.error("Error creating task: {}", e.getMessage());
            return false;
        }
    }

    public boolean createTask(TaskDTO taskDTO, Category category, User user) {
        try {
            taskRepository.save(new Task(taskDTO.title(), taskDTO.content(), category, user));
            return true;
        } catch (Exception e) {
            logger.error("Error creating task of category: {}", e.getMessage());
            return false;
        }
    }

    public List<TaskResponseDTO> getAllTasks(Long userid) {
        return taskRepository.findAllByUserId(userid)
                .stream()
                .filter(task -> task.getCategory() == null)
                .map(task -> new TaskResponseDTO(
                        task.getTaskId(),
                        task.getTitle(),
                        task.getContent(),
                        task.getStatus(),
                        task.getCreatedAt()
                ))
                .toList();
    }

    public HttpStatus updateTasks(Long taskId, TaskDTO taskDTO, User user) {
        try {
            Optional<Task> task = taskRepository.findByIdAndUserId(taskId, user.getId());
            if (task.isPresent()) {
                task.get().setTitle(taskDTO.title());
                task.get().setContent(taskDTO.content());
                taskRepository.save(task.get());
                return HttpStatus.OK;
            }
            return HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            logger.error("Error while updating Task: {}", e.getMessage());
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @Transactional
    public boolean deleteTask(Long taskId, Long userId) {
        try {
            taskRepository.deleteTaskByIdAndUserId(taskId, userId);
            return true;
        } catch (Exception e) {
            logger.error("Error while deleting Task: {}", e.getMessage());
            return false;
        }
    }

    public List<TaskCategoryResponseDTO> getTasksOfCategory(Long categoryId, Long userId) {
        try {
            return taskRepository.findAllByCategoryIdAndUserId(categoryId, userId)
                    .stream().map(task -> new TaskCategoryResponseDTO(
                            task.getTaskId(),
                            task.getCategory().getCategoryId(),
                            task.getTitle(),
                            task.getContent(),
                            task.getStatus(),
                            task.getCreatedAt()
                    )).toList();
        } catch (Exception e) {
            logger.error("Error while getting all Tasks of Category {}", e.getMessage());
            return List.of();
        }
    }

    public boolean checkValidTask(String title) {
        if (title.isEmpty()) {
            System.out.println("Error: Title cannot be empty");
            return false;
        }

        if (title.length() > 100) {
            System.out.println("Error: Title is too long");
            return false;
        }
        return true;
    }

}
