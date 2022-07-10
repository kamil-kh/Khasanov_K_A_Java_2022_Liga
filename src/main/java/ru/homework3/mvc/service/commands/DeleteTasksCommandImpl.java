package ru.homework3.mvc.service.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.homework3.mvc.dto.UserDto;
import ru.homework3.mvc.repo.UserRepository;
import ru.homework3.mvc.service.Command;

@Component
@RequiredArgsConstructor
public class DeleteTasksCommandImpl implements Command {
    private final UserRepository userRepository;
    private final String COMMAND_NAME = "delete tasks";
    @Override
    public String run(UserDto dto) {
        if (userRepository.clearTasks()) {
            return "<p style='color:green;'>Удачное удаление задач.</p>";
        } else {
            return "<p style='color:red;'>Неудачное удаление задач.</p>";
        }
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }
}
