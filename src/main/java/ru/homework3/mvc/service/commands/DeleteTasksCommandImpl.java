package ru.homework3.mvc.service.commands;

import lombok.RequiredArgsConstructor;
import ru.homework3.mvc.dto.DtoEntities;
import ru.homework3.mvc.repo.UserRepository;
import ru.homework3.mvc.service.Command;

@RequiredArgsConstructor
public class DeleteTasksCommandImpl implements Command {
    private final UserRepository userRepository;
    private final String COMMAND_NAME = "delete tasks";
    @Override
    public String run(String commandName, DtoEntities dto) {
        if (!(COMMAND_NAME.compareTo(commandName.toLowerCase()) == 0)) {
            return "";
        }
        if (userRepository.clearTasks()) {
            return "<p style='color:green;'>Удачное удаление задач.</p>";
        } else {
            return "<p style='color:red;'>Неудачное удаление задач.</p>";
        }
    }
}
