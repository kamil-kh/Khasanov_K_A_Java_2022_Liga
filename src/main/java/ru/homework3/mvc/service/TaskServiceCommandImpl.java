package ru.homework3.mvc.service;

import ru.homework3.mvc.dto.DtoEntities;
import ru.homework3.mvc.model.Task;
import ru.homework3.mvc.repo.UserRepository;
import ru.homework3.mvc.service.commands.CreateTaskCommandImpl;
import ru.homework3.mvc.service.commands.DeleteTaskCommandImpl;
import ru.homework3.mvc.service.commands.UpdateTaskCommandImpl;

import java.util.ArrayList;
import java.util.List;

public class TaskServiceCommandImpl implements Command {
    private final List<Command> commands = new ArrayList<>();
    private final UserRepository userRepository;
    private final String COMMAND_NAME = "get tasks";
    private final String menuTasks = "Команды:<br>" +
            "1. Создать задачу [?command=create%20task&user-id={id пользователя}&task-header={имя задачи}" +
            "&task-description={описание задачи}&task-date={дата в формате dd.MM.yyyy}]:<br>" +
            "2. Обновить задачу [?command=update%20task&user-id={id пользователя}&task-id={id задачи}" +
            "&task-description={описание задачи}&task-date={дата в формате dd.MM.yyyy}&task-status={статус задачи: новая, в работе, готово}:<br>" +
            "3. Удалить задачу [?command=delete%20task&user-id={id пользователя}&task-id={id задачи}]:<br>";

    public TaskServiceCommandImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        commands.add(new CreateTaskCommandImpl(userRepository));
        commands.add(new UpdateTaskCommandImpl(userRepository));
        commands.add(new DeleteTaskCommandImpl(userRepository));
    }

    public String run(String commandName, DtoEntities dto) {
        String list = "";
        for (Command command: commands) {
            list += command.run(commandName, dto);
        }
        if (!(COMMAND_NAME.compareTo(commandName.toLowerCase()) == 0)
                && list.compareTo("") == 0
        ) {
            return "";
        }
        Integer idUser = dto.getUser().getId();
        if (idUser == null) {
            return "<p style='color:red;'>Поле 'user-id' должно быть заполнено.</p>";
        }
        if (!userRepository.checkIdUser(idUser)) {
            return "<p style='color:red;'>Пользователя с id=" + idUser + " не существует.</p>";
        }
        return "<hr style='color:black;width:100%;height:2px;'>" + getTasks(idUser, dto.getFilter()) +
                "<br>" + menuTasks + "<br>" + list;
    }

    private String getTasks (Integer idUser, Integer filter) {
        String tasksStr = "";
        List<Task> tasks = userRepository.getTasks(idUser);
        if (filter == 1) {
            tasks = tasks.stream().sorted((task1, task2) -> task1.getId() - task2.getId()).toList();
        } else {
            tasks = tasks.stream().sorted((task1, task2) -> task1.getStatus().compareTo(task2.getStatus())).toList();
        }
        for (Task task: tasks) {
            tasksStr += task.toString() + "<br>";
        }
        return tasksStr;
    }
}
