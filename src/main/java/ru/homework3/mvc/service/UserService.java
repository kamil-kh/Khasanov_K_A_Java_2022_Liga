package ru.homework3.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.homework3.mvc.model.Task;
import ru.homework3.mvc.model.User;
import ru.homework3.mvc.repo.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public String getUsers() {
        String usersStr = "";
        List<User> users = userRepository.getUsers();
        for (User user: users) {
            usersStr += user.toString() +
                    " {<a href='/" +
                    user.getId() +
                    "?filter=1'>Вывести задачи</a> | <a href='" +
                    user.getId() +
                    "/delete_user'>Удалить</a>}<br/>";
        }
        return usersStr;
    }

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

    public boolean createUser(String name) {
        return userRepository.addUser(name);
    }

    public boolean deleteUser(int idUser) {
        return userRepository.removeUser(idUser);
    }

    public boolean deleteUsers() {
        return userRepository.clearUsers();
    }

    public boolean createTask(int idUser, String header, String description, String date) {
        return userRepository.addTask(idUser, header, description, date);
    }

    public boolean deleteTask(int idUser, int idTask) {
        return userRepository.removeTask(idUser, idTask);
    }

    public boolean deleteTasks() {
        return userRepository.clearTasks();
    }

    public boolean updateTask(int idUser, int idTask, String description, String date, String status) {
        return userRepository.changeTask(idUser, idTask, description, date, status);
    }
}
