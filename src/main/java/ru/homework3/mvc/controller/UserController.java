package ru.homework3.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.homework3.mvc.dto.UserDto;
import ru.homework3.mvc.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> index() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUser(@PathVariable Integer userId) {
        UserDto dto = userService.getUser(userId);
        if (dto == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> create(@RequestBody UserDto dto) {
        return new ResponseEntity<>(userService.createUser(dto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/{userId}")
    public ResponseEntity<String> delete(@PathVariable Integer userId) {
        return new ResponseEntity<>(userService.deleteUser(userId), HttpStatus.OK);
    }

    @DeleteMapping(value = "/users")
    public ResponseEntity<String> deletes() {
        userService.deleteUsers();
        return new ResponseEntity<>("Done!", HttpStatus.OK);
    }
}
