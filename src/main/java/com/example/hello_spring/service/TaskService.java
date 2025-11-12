package com.example.hello_spring.service;

import com.example.hello_spring.dto.request.TaskCreateDTO;
import com.example.hello_spring.dto.request.TaskUpdateDTO;
import com.example.hello_spring.dto.response.TaskResponseDTO;
import com.example.hello_spring.entity.Task;
import com.example.hello_spring.exception.ResourceNotFoundException;
import com.example.hello_spring.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;

    private TaskResponseDTO convertToDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted());
    }

    // create task
    @Transactional
    public TaskResponseDTO createTask(TaskCreateDTO taskCreateDTO) {
        log.info("create task: {}", taskCreateDTO.title());
        Task task = Task.builder()
                .title(taskCreateDTO.title())
                .description(taskCreateDTO.description())
                .completed(false)
                .build();
        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }

    // get task by id
    @Transactional(readOnly = true)
    public TaskResponseDTO getTaskById(Long id) {
        log.info("get task by id: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return convertToDTO(task);
    }

    // get all tasks
    @Transactional(readOnly = true)
    public Page<TaskResponseDTO> getAllTasks(Pageable pageable) {
        log.info("get all tasks");
        Page<Task> taskPage = taskRepository.findAll(pageable);

        return taskPage.map(this::convertToDTO);
    }

    // update task
    @Transactional
    public TaskResponseDTO updateTask(Long id, TaskUpdateDTO updateDTO) {
        log.info("update task: {}", id);
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        existingTask.setTitle(updateDTO.title());
        existingTask.setDescription(updateDTO.description());
        existingTask.setCompleted(updateDTO.completed());

        Task updatedTask = taskRepository.save(existingTask);
        return convertToDTO(updatedTask);
    }

    // delete task
    @Transactional
    public void deleteTask(Long id) {
        log.info("delete task: {}", id);
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }
}
