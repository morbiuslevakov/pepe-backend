package com.pepe.miniapp.controllers;

import com.pepe.miniapp.authentication.details.UserDetailsImpl;
import com.pepe.miniapp.payload.request.AddTaskRequest;
import com.pepe.miniapp.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/tasks")
public class TasksController {
    private final TaskService taskService;

    public TasksController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/add-task")
    public ResponseEntity<?> addTask(@RequestBody AddTaskRequest addTaskRequest) {
        return taskService.addTask(addTaskRequest);
    }

    @GetMapping("/complete")
    public ResponseEntity<?> completeTask(@RequestParam("taskId") String taskId) {
        Long id = Long.valueOf(((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        return taskService.completeTask(id, taskId);
    }

    @GetMapping("/get-tasks")
    public ResponseEntity<?> getTasks() {
        Long id = Long.valueOf(((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        return taskService.getTasks(id);
    }
}
