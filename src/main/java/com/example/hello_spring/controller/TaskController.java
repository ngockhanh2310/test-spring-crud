package com.example.hello_spring.controller;

import com.example.hello_spring.dto.request.TaskCreateDTO;
import com.example.hello_spring.dto.request.TaskUpdateDTO;
import com.example.hello_spring.dto.response.TaskResponseDTO;
import com.example.hello_spring.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    // create task
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDTO createTask(@Valid @RequestBody TaskCreateDTO taskCreateDTO) {
        return taskService.createTask(taskCreateDTO);
    }

    // get task by id
    @GetMapping("/{id}")
    public TaskResponseDTO getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    // get all task
    @GetMapping
    public Page<TaskResponseDTO> getAllTask(Pageable pageable) {
        return taskService.getAllTasks(pageable);
    }

    // update task
    @PutMapping("/{id}")
    public TaskResponseDTO updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateDTO updateDTO) {
        return taskService.updateTask(id, updateDTO);
    }

    // delete task
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
