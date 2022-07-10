package ru.homework3.mvc.service.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.homework3.mvc.dto.UserDto;
import ru.homework3.mvc.model.Task;
import ru.homework3.mvc.repo.UserRepository;
import ru.homework3.mvc.service.Command;

@Component
@RequiredArgsConstructor
public class DeleteTaskCommandImpl implements Command {
    private final UserRepository userRepository;
    private final String COMMAND_NAME = "delete task";

    @Override
    public String run(UserDto dto) {
        Task task = dto.getTask();
        Integer idUser = task.getIdUser();
        if (idUser == null) {
            return "<p style='color:red;'>Поле 'user-id' должно быть заполнено.</p>";
        }
        if (!userRepository.checkIdUser(idUser)) {
            return "<p style='color:red;'>Пользователя с id=" + idUser + " не существует.</p>";
        }
        Integer idTask = task.getId();
        if (idTask == null) {
            return "<p style='color:red;'>Поле 'task-id' должно быть заполнено.</p>";
        }
        if (!userRepository.checkIdTask(idTask)) {
            return "<p style='color:red;'>Задачи с id=" + idTask + " не существует.</p>";
        }
        if (userRepository.removeTask(idUser, idTask)) {
            return "<p style='color:green;'>Удачное удаление задачи.</p>";
        } else {
            return "<p style='color:red;'>Неудачное удаление задачи.</p>";
        }
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }
}
