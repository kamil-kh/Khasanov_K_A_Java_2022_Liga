package ru.homework3.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.homework3.mvc.dto.TaskDto;
import ru.homework3.mvc.dto.transfer.*;
import ru.homework3.mvc.service.TaskService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/")
public class TaskController {
    private final TaskService taskService;

    @GetMapping(value = "/{userId}/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskDto>> index(@PathVariable Integer userId) {
        return new ResponseEntity<>(taskService.getTasks(userId), HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/tasks/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> getTask(@PathVariable Integer userId, @PathVariable Integer taskId) {
        return new ResponseEntity<>(taskService.getTask(userId, taskId), HttpStatus.OK);
    }

    @PostMapping(value = "/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> create(@Validated(New.class) @RequestBody TaskDto dto,
                                          @PathVariable Integer userId) {
        return new ResponseEntity<>(taskService.createTask(dto, userId), HttpStatus.OK);
    }

    @PutMapping(value = "/{userId}/tasks/{taskId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> update(@Validated(Update.class) @RequestBody TaskDto dto,
                                          @PathVariable Integer userId,
                                          @PathVariable Integer taskId) {
        dto.setId(taskId);
        TaskDto dtoUpdate = taskService.updateTask(dto, userId);
        if (dtoUpdate == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(dtoUpdate, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{userId}/tasks/{taskId}")
    public ResponseEntity<String> delete(@PathVariable Integer userId, @PathVariable Integer taskId) {
        return new ResponseEntity<>(taskService.deleteTask(userId, taskId), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{userId}/tasks")
    public ResponseEntity<String> deletes(@PathVariable Integer userId) {
        taskService.deleteTasks(userId);
        return new ResponseEntity<>("Done!", HttpStatus.OK);
    }
}
