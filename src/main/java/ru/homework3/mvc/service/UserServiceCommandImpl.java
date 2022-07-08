package ru.homework3.mvc.service;

import org.springframework.stereotype.Service;
import ru.homework3.mvc.dto.DtoEntities;
import ru.homework3.mvc.model.User;
import ru.homework3.mvc.repo.UserRepository;
import ru.homework3.mvc.service.commands.CreateUserCommandImpl;
import ru.homework3.mvc.service.commands.DeleteTasksCommandImpl;
import ru.homework3.mvc.service.commands.DeleteUserCommandImpl;
import ru.homework3.mvc.service.commands.DeleteUsersCommandImpl;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceCommandImpl implements Command {
    private final List<Command> commands = new ArrayList<>();
    private final UserRepository userRepository;
    private final String menuUsers = "Команды:<br>" +
            "1. Вывести задачи пользователя [?command=get%20tasks&user-id={id пользователя}" +
            "&filter={1(по умолчанию) - сортировка по id); 2 - сортировка по статусу}]:<br>" +
            "2. Создать пользователя [?command=create%20user&user-name={имя с большой буквы}]:<br>" +
            "3. Удалить пользователя [?command=delete%20user&user-id={id пользователя}]:<br>" +
            "4. Удалить пользователей [?command=delete%20users]:<br>" +
            "5. Удалить задачи [?command=delete%20tasks]:<br>";

    public UserServiceCommandImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        commands.add(new CreateUserCommandImpl(userRepository));
        commands.add(new DeleteUserCommandImpl(userRepository));
        commands.add(new DeleteUsersCommandImpl(userRepository));
        commands.add(new DeleteTasksCommandImpl(userRepository));
        commands.add(new TaskServiceCommandImpl(userRepository));
    }

    @Override
    public String run(String commandName, DtoEntities dto) {
        if (commandName == null) {
            return getUsers() + "<br>" + menuUsers;
        }
        String list = "";
        for (Command command: commands) {
            list += command.run(commandName, dto);
        }
        return getUsers() + "<br>" + menuUsers + "<br>" + list;
    }

    private String getUsers() {
        String usersStr = "";
        List<User> users = userRepository.getUsers();
        for (User user: users) {
            usersStr += user.toString() + "<br>";
        }
        return usersStr;
    }
}
