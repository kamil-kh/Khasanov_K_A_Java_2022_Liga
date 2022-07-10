package ru.homework3.mvc.service.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.homework3.mvc.dto.UserDto;
import ru.homework3.mvc.repo.UserRepository;
import ru.homework3.mvc.service.Command;

@Component
@RequiredArgsConstructor
public class DeleteUserCommandImpl implements Command {
    private final UserRepository userRepository;
    private final String COMMAND_NAME = "delete user";

    @Override
    public String run(UserDto dto) {
        Integer id = dto.getUser().getId();
        if (id == null) {
            return "<p style='color:red;'>Поле 'user-id' должно быть заполнено.</p>";
        }
        if (!userRepository.checkIdUser(id)) {
            return "<p style='color:red;'>Пользователя с id=" + id + " не существует.</p>";
        }
        if (userRepository.removeUser(id)) {
            return "<p style='color:green;'>Удачное удаление пользователя.</p>";
        } else {
            return "<p style='color:red;'>Неудачное удаление пользователя.</p>";
        }
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }
}
