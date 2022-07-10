package ru.homework3.mvc.service;

import ru.homework3.mvc.dto.UserDto;

public interface Command {
    String run(UserDto dto);

    String getCommandName();
}
