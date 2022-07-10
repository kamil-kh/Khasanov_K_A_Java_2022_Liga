package ru.homework3.mvc.service.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.homework3.mvc.dto.UserDto;
import ru.homework3.mvc.model.Task;
import ru.homework3.mvc.repo.UserRepository;
import ru.homework3.mvc.service.Command;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetTasksCommandImpl implements Command {
    private final UserRepository userRepository;
    private final String COMMAND_NAME = "get tasks";

    @Override
    public String run(UserDto dto) {
        String tasksStr = "";
        List<Task> tasks = userRepository.getTasks(dto.getUser().getId());
        if (dto.getFilter() == 1) {
            tasks = tasks.stream().sorted((task1, task2) -> task1.getId() - task2.getId()).toList();
        } else {
            tasks = tasks.stream().sorted((task1, task2) -> task1.getStatus().compareTo(task2.getStatus())).toList();
        }
        for (Task task: tasks) {
            tasksStr += task.toString() + "<br>";
        }
        return tasksStr;
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }
}
