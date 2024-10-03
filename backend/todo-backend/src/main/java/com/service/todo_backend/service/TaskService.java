package com.service.todo_backend.service;

import com.service.todo_backend.model.Task;
import com.service.todo_backend.model.User;
import com.service.todo_backend.payload.in.TaskDTO;
import com.service.todo_backend.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            taskRepository.save(new Task(taskDTO.title(), taskDTO.description(), user));
            return true;
        } catch (Exception e) {
            logger.error("Error creating task: {}", e.getMessage());
            return false;
        }
    }

    public List<Task> getAllTasks(Long userid) {
        return taskRepository.findAllByUserId(userid)
                .stream()
                .filter(task -> task.getCategory().getCategoryId() == 0
                        || task.getCategory().getCategoryId() == null)
                .toList();
    }

    public boolean updateTasks(Long taskId,TaskDTO taskDTO, User user) {
        try{
            Optional<Task> task = taskRepository.findByIdAndUserId(taskId, user.getId());
            if (task.isPresent()){
                task.get().setTitle(taskDTO.title());
                task.get().setContent(taskDTO.description());
                taskRepository.save(task.get());
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("Error while updating Task: {}", e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean deleteTask(Long taskId,Long userId){
        try {
            taskRepository.deleteTaskByIdAndUserId(taskId,userId);
            return true;
        } catch (Exception e) {
            logger.error("Error while deleting Task: {}", e.getMessage());
            return false;
        }
    }

    public boolean checkValidTask(TaskDTO taskDTO) {
        if (taskDTO.title().isEmpty()) {
            return false;
        }
        return taskDTO.title().length() <= 100;
    }
}