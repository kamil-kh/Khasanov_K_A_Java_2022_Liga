package ru.homework3.mvc.service.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.homework3.mvc.dto.UserDto;
import ru.homework3.mvc.repo.UserRepository;
import ru.homework3.mvc.service.Command;

@Component
@RequiredArgsConstructor
public class DeleteUsersCommandImpl implements Command {
    private final UserRepository userRepository;
    private final String COMMAND_NAME = "delete users";
    @Override
    public String run(UserDto dto) {
        if (userRepository.clearUsers()) {
            return "<p style='color:green;'>Удачное удаление пользователей.</p>";
        } else {
            return "<p style='color:red;'>Неудачное удаление пользователей.</p>";
        }
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }
}
