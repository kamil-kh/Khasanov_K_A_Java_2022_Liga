package ru.homework3.mvc.service.commands;

import lombok.RequiredArgsConstructor;
import ru.homework3.mvc.dto.DtoEntities;
import ru.homework3.mvc.repo.UserRepository;
import ru.homework3.mvc.service.Command;

@RequiredArgsConstructor
public class DeleteUsersCommandImpl implements Command {
    private final UserRepository userRepository;
    private final String COMMAND_NAME = "delete users";
    @Override
    public String run(String commandName, DtoEntities dto) {
        if (!(COMMAND_NAME.compareTo(commandName.toLowerCase()) == 0)) {
            return "";
        }
        if (userRepository.clearUsers()) {
            return "<p style='color:green;'>Удачное удаление пользователей.</p>";
        } else {
            return "<p style='color:red;'>Неудачное удаление пользователей.</p>";
        }
    }
}
