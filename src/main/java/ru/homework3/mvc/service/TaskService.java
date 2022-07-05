package ru.homework3.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.homework3.mvc.model.Task;
import ru.homework3.mvc.repo.UserRepository;
import ru.homework3.mvc.utils.ResponseCode;
import ru.homework3.mvc.validator.Validator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final UserRepository userRepository;

    public String getTasks(int idUser, int filter) {
        String tasksStr = "";
        List<Task> tasks = userRepository.getTasks(idUser);
        if (filter == 1) {
            tasks = tasks.stream().sorted((task1, task2) -> task1.getId() - task2.getId()).toList();
        } else {
            tasks = tasks.stream().sorted((task1, task2) -> task1.getStatus().compareTo(task2.getStatus())).toList();
        }
        for (Task task: tasks) {
            tasksStr += task.toString() +
                    " {<a href='/" + idUser +
                    "/update_task/" + task.getId() +
                    "'>Изменить</a> | <a href='/" + idUser +
                    "/delete_task/" + + task.getId() +
                    "?filter=" + filter + "'>Удалить</a>}<br/>";
        }
        return tasksStr;
    }

    public Task getTask(int idUser, int idTask) {
        return userRepository.getTask(idUser, idTask);
    }

    public ResponseCode createTask(Task task) {
        if (Validator.validateDate(task.getDate())) {
            return getCode(userRepository.addTask(task));
        } else {
            return ResponseCode.ERROR_VALIDATE;
        }
    }

    public ResponseCode deleteTask(int idUser, int idTask) {
        return getCode(userRepository.removeTask(idUser, idTask));
    }

    public ResponseCode deleteTasks() {
        return getCode(userRepository.clearTasks());
    }

    public ResponseCode updateTask(Task task) {
        if (Validator.validateDate(task.getDate())) {
            return getCode(userRepository.changeTask(task));
        } else {
            return ResponseCode.ERROR_VALIDATE;
        }
    }

    private ResponseCode getCode(boolean isSuccess) {
        if (isSuccess) {
            return ResponseCode.SUCCESS;
        } else {
            return ResponseCode.ERROR_WRITE_OR_READ_CSV;
        }
    }
}
