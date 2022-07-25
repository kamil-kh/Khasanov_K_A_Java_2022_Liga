package ru.homework3.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.homework3.mvc.dto.TaskDto;
import ru.homework3.mvc.dto.transfer.*;
import ru.homework3.mvc.service.UserTasksService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserTasksController {
    private final UserTasksService userTasksService;

    @GetMapping(value = "/{userId}/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TaskDto> index(@PathVariable Integer userId) {
        return userTasksService.getTasks(userId);
    }

    @GetMapping(value = "/{userId}/tasks/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TaskDto getTask(@PathVariable Integer userId, @PathVariable Integer taskId) throws BusinessException {
        TaskDto dto = userTasksService.getTask(userId, taskId);
        if (dto == null) {
            throw new BusinessException("Task not found!");
        }
        return dto;
    }

    @PostMapping("/{userId}/tasks")
    public String create(@Validated(New.class) @RequestBody TaskDto dto,
                                          @PathVariable Integer userId) {
        return userTasksService.createTask(dto, userId);
    }

    @PutMapping("/{userId}/tasks/{taskId}")
    public String update(@Validated(Update.class) @RequestBody TaskDto dto,
                                          @PathVariable Integer userId,
                                          @PathVariable Integer taskId)
            throws BusinessException {
        dto.setId(taskId);
        String message = userTasksService.updateTask(dto, userId);
        if (message == null) {
            throw new BusinessException("Task not found!");
        }
        return message;
    }

    @DeleteMapping("/{userId}/tasks/{taskId}")
    public String delete(@PathVariable Integer userId, @PathVariable Integer taskId) {
        return userTasksService.deleteTask(userId, taskId);
    }

    @DeleteMapping("/{userId}/tasks")
    public String deletes(@PathVariable Integer userId) {
        return userTasksService.deleteTasks(userId);
    }
}
