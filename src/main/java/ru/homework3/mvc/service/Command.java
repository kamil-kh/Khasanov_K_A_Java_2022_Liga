package ru.homework3.mvc.service;

import ru.homework3.mvc.dto.DtoEntities;

public interface Command {
    String run(String commandName, DtoEntities dto);
}
