package com.pepe.miniapp.services;

import com.pepe.miniapp.exceptions.DefaultException;
import com.pepe.miniapp.models.Task;
import com.pepe.miniapp.models.User;
import com.pepe.miniapp.payload.request.AddTaskRequest;
import com.pepe.miniapp.pojo.TaskPJ;
import com.pepe.miniapp.repositories.TaskRepository;
import com.pepe.miniapp.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public TaskService(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public ResponseEntity<?> addTask(AddTaskRequest addTaskRequest) {
        Task task = new Task(addTaskRequest.getTitle(), addTaskRequest.getReward(), addTaskRequest.getLink());
        taskRepository.save(task);
        return ResponseEntity.ok().body("Task added successfully");
    }

    public ResponseEntity<?> getTasks(Long userId) {
        List<Task> tasks = taskRepository.findAll();
        User user = userRepository.findByTelegramId(userId).orElseThrow(() -> new DefaultException("Internal server error"));
        List<TaskPJ> taskPJS = new ArrayList<>();
        for (Task task : tasks) {
            boolean isCompleted = false;
            for (Task userTask : user.getTasks()) {
                if (userTask.getId().equals(task.getId())) {
                    isCompleted = true;
                }
            }
            TaskPJ taskPJ = new TaskPJ(task.getId(), task.getTitle(), task.getReward(), task.getLink(), isCompleted);
            taskPJS.add(taskPJ);
        }
        return ResponseEntity.ok().body(taskPJS);
    }

    public ResponseEntity<?> completeTask(Long userId, String taskId) {
        User user = userRepository.findByTelegramId(userId).orElseThrow(() -> new DefaultException("User not found"));
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new DefaultException("Task not found"));
        user.getTasks().add(task);
        if (user.getReff() != null) {
            user.getReff().setScore(user.getReff().getScore().add(new BigInteger(String.valueOf(task.getReward())).multiply(new BigInteger("0.1"))));
            userRepository.save(user.getReff());
        }
        user.setScore(user.getScore().add(new BigInteger(String.valueOf(task.getReward()))));
        userRepository.save(user);
        return ResponseEntity.ok().body("Task completed");
    }
}
