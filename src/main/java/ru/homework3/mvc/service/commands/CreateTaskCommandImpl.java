package ru.homework3.mvc.service.commands;

import lombok.RequiredArgsConstructor;
import ru.homework3.mvc.dto.DtoEntities;
import ru.homework3.mvc.model.Task;
import ru.homework3.mvc.repo.UserRepository;
import ru.homework3.mvc.service.Command;
import ru.homework3.mvc.util.validator.Validator;

@RequiredArgsConstructor
public class CreateTaskCommandImpl implements Command {
    private final UserRepository userRepository;
    private final String COMMAND_NAME = "create task";

    @Override
    public String run(String commandName, DtoEntities dto) {
        if (!(COMMAND_NAME.compareTo(commandName.toLowerCase()) == 0)) {
            return "";
        }
        Task task = dto.getTask();
        Integer idUser = task.getIdUser();
        if (idUser == null) {
            return "<p style='color:red;'>Поле 'user-id' должно быть заполнено.</p>";
        }
        if (!userRepository.checkIdUser(idUser)) {
            return "<p style='color:red;'>Пользователя с id=" + idUser + " не существует.</p>";
        }
        if (Validator.isEmpty(task.getHeader())) {
            return "<p style='color:red;'>Поле 'task-header' должно быть заполнено.</p>";
        }
        if (Validator.isEmpty(task.getDescription())) {
            return "<p style='color:red;'>Поле 'task-description' должно быть заполнено.</p>";
        }
        if (!Validator.validateDate(task.getDate())) {
            return "<p style='color:red;'>Неверный формат даты! Правильный формат: dd.MM.yyyy.</p>";
        }
        if (userRepository.addTask(task)) {
            return "<p style='color:green;'>Удачное добавление задачи.</p>";
        } else {
            return "<p style='color:red;'>Неудачное добавление задачи.</p>";
        }
    }
}