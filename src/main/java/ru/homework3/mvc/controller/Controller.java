package ru.homework3.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.homework3.mvc.dto.UserDto;
import ru.homework3.mvc.service.UserService;

@RestController
@RequiredArgsConstructor
public class Controller {
    private final UserService userService;

    @GetMapping("/")
    public String index(@RequestParam(value = "command", required = false) String command,
                        @RequestParam(value = "user-id", required = false) Integer userId,
                        @RequestParam(value = "task-id", required = false) Integer taskId,
                        @RequestParam(value = "filter", required = false) Integer filter,
                        @RequestParam(value = "user-name", required = false) String userName,
                        @RequestParam(value = "task-header", required = false) String taskHeader,
                        @RequestParam(value = "task-description", required = false) String taskDescription,
                        @RequestParam(value = "task-date", required = false) String taskDate,
                        @RequestParam(value = "task-status", required = false) String taskStatus) {
        UserDto dto = new UserDto();
        dto.setUser(userId, userName);
        dto.setTask(taskId, userId, taskHeader, taskDescription, taskDate, taskStatus);
        dto.setFilter(filter);
        return userService.index(command, dto);
    }
}
