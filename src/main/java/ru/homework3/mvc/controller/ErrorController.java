package ru.homework3.mvc.controller;

import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@NoArgsConstructor
public class ErrorController {

    @GetMapping("/error_message")
    public String error(@RequestParam("message") String message) {
        return message + "<br/><a href='/'>Главная</a>";
    }
}
