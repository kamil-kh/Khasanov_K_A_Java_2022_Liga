package ru.homework3.mvc.service.commands;

import lombok.RequiredArgsConstructor;
import ru.homework3.mvc.dto.DtoEntities;
import ru.homework3.mvc.repo.UserRepository;
import ru.homework3.mvc.service.Command;

@RequiredArgsConstructor
public class DeleteUserCommandImpl implements Command {
    private final UserRepository userRepository;
    private final String COMMAND_NAME = "delete user";

    @Override
    public String run(String commandName, DtoEntities dto) {
        if (!(COMMAND_NAME.compareTo(commandName.toLowerCase()) == 0)) {
            return "";
        }
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
}
