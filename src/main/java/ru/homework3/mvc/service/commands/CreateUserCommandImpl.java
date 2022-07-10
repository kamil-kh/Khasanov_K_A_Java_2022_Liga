package ru.homework3.mvc.service.commands;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import ru.homework3.mvc.dto.UserDto;
import ru.homework3.mvc.model.User;
import ru.homework3.mvc.repo.UserRepository;
import ru.homework3.mvc.service.Command;
import ru.homework3.mvc.util.validator.Validator;

@Component
@RequiredArgsConstructor
public class CreateUserCommandImpl implements Command {
    private final UserRepository userRepository;
    private final String COMMAND_NAME = "create user";

    @Override
    public String run(UserDto dto) {
        User user = dto.getUser();
        if (!Validator.validateName(user.getName())) {
            return "<p style='color:red;'>Неверный формат имени!" +
                    " Имя должно начинаться с большой буквы и не иметь пробелы.</p>";
        }
        if (userRepository.addUser(user)) {
            return "<p style='color:green;'>Удачное добавление пользователя.</p>";
        } else {
            return "<p style='color:red;'>Неудачное добавление пользователя.</p>";
        }
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }
}
