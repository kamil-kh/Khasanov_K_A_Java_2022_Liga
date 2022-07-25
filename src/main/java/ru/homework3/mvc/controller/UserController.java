package ru.homework3.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.homework3.mvc.dto.UserDto;
import ru.homework3.mvc.dto.transfer.New;
import ru.homework3.mvc.dto.transfer.Update;
import ru.homework3.mvc.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> index() {
        return userService.getUsers();
    }

    @GetMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto getUser(@PathVariable Integer userId) throws BusinessException {
        UserDto dto = userService.getUser(userId);
        if (dto == null) {
            throw new BusinessException("User not found!");
        }
        return dto;
    }

    @PostMapping("/users")
    public String create(@Validated(New.class) @RequestBody UserDto dto) {
        return userService.createUser(dto);
    }

    @PutMapping("/users/{userId}")
    public String update(@Validated(Update.class) @RequestBody UserDto dto, @PathVariable Integer userId)
            throws BusinessException {
        dto.setId(userId);
        String message = userService.updateUser(dto);
        if (message == null) {
            throw new BusinessException("User not found!");
        }
        return message;
    }

    @DeleteMapping("/users/{userId}")
    public String delete(@PathVariable Integer userId) {
        return userService.deleteUser(userId);
    }

    @DeleteMapping("/users")
    public String deletes() {
        return userService.deleteUsers();
    }
}
