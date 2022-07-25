package ru.homework3.mvc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.homework3.mvc.controller.BusinessException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleException(BusinessException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
