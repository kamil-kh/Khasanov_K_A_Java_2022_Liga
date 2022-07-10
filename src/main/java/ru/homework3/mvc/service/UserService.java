package ru.homework3.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.homework3.mvc.dto.UserDto;
import ru.homework3.mvc.model.User;
import ru.homework3.mvc.repo.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final List<Command> commands;
    private final UserRepository userRepository;
    private final String MENU_USERS = "Команды:<br>" +
            "1. Создать пользователя [?command=create%20user&user-name={имя с большой буквы}]:<br>" +
            "2. Удалить пользователя [?command=delete%20user&user-id={id пользователя}]:<br>" +
            "3. Удалить пользователей [?command=delete%20users]:<br>" +
            "4. Вывести задачи пользователя [?command=get%20tasks&user-id={id пользователя}" +
            "&filter={1(по умолчанию) - сортировка по id); 2 - сортировка по статусу}]:<br>" +
            "5. Создать задачу [?command=create%20task&user-id={id пользователя}&task-header={имя задачи}" +
            "&task-description={описание задачи}&task-date={дата в формате dd.MM.yyyy}]:<br>" +
            "6. Обновить задачу [?command=update%20task&user-id={id пользователя}&task-id={id задачи}" +
            "&task-description={описание задачи}&task-date={дата в формате dd.MM.yyyy}&task-status={статус задачи: новая, в работе, готово}:<br>" +
            "7. Удалить задачу [?command=delete%20task&user-id={id пользователя}&task-id={id задачи}]:<br>" +
            "8. Удалить задачи [?command=delete%20tasks]:<br>";

    public String index(String commandName, UserDto dto) {
        if (commandName == null) {
            return getUsers() + "<br>" + MENU_USERS;
        }
        String data = "<p style='color:red;'>Неверная команда!</p>";
        for (Command command : commands) {
            if (commandName.toLowerCase().compareTo(command.getCommandName()) == 0) {
                data = command.run(dto);
                break;
            }
        }
        return getUsers() + "<br>" + MENU_USERS + "<br>" + data;
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
