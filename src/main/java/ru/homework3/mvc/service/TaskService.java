package ru.homework3.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.homework3.mvc.controller.ResponseCode;
import ru.homework3.mvc.model.Task;
import ru.homework3.mvc.model.User;
import ru.homework3.mvc.repo.TaskRepository;
import ru.homework3.mvc.repo.UserRepository;
import ru.homework3.mvc.utils.validator.Validator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public String getTasks(Integer idUser, Integer filter) {
        String tasksStr = "";
        List<Task> tasks = userRepository.getReferenceById(idUser).getTasks();
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

    public Task getTask(Integer idUser, Integer idTask) {
        return taskRepository.getReferenceById(idTask);
    }

    public ResponseCode createTask(Task task, Integer idUser) {
        if (Validator.validateDate(task.getDate())) {
            task.setUser(userRepository.getReferenceById(idUser));
            taskRepository.save(task);
            return ResponseCode.SUCCESS;
        } else {
            return ResponseCode.ERROR_VALIDATE;
        }
    }

    public void deleteTask(Integer idTask) {
        taskRepository.deleteById(idTask);
    }

    public void deleteTasks() {
        taskRepository.deleteAll();
    }

    public ResponseCode updateTask(Task newTask, Integer idTask) {
        if (Validator.validateDate(newTask.getDate())) {
            Task oldTask = taskRepository.getReferenceById(idTask);
            oldTask.setDescription(newTask.getDescription());
            oldTask.setDate(newTask.getDate());
            oldTask.setStatus(newTask.getStatus());
            taskRepository.save(oldTask);
            return ResponseCode.SUCCESS;
        } else {
            return ResponseCode.ERROR_VALIDATE;
        }
    }
}
