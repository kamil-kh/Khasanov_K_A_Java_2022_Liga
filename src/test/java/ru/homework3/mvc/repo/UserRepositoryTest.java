package ru.homework3.mvc.repo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.homework3.mvc.model.Task;
import ru.homework3.mvc.model.User;
import ru.homework3.mvc.service.TaskService;
import ru.homework3.mvc.service.UserService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private final String dirUsers = "csv/test/UsersTest.csv";
    private final String dirTasks = "csv/test/TasksTest.csv";
    private final UserRepository userRepository = new UserRepository(dirUsers, dirTasks);

    @AfterEach
    private void preTest() {
        try {
            BufferedWriter writer = Files.newBufferedWriter(
                    Path.of(dirUsers), StandardCharsets.UTF_8,
                    StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING
            );
            writer.close();
            writer = Files.newBufferedWriter(
                    Path.of(dirTasks), StandardCharsets.UTF_8,
                    StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING
            );
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Добавление пользователя")
    void add_User() {
        User user = getUser();
        assertTrue(userRepository.addUser(user));
    }

    @Test
    @DisplayName("Возвращает пользователей")
    void get_Users() {
        User user = getUser();
        userRepository.addUser(user);
        List<User> users = new ArrayList<>();
        users.add(user);
        assertEquals(users, userRepository.getUsers());
    }

    @Test
    @DisplayName("Удаление пользователя")
    void remove_User() {
        User user = getUser();
        userRepository.addUser(user);
        assertTrue(userRepository.removeUser(user.getId()));
    }

    @Test
    @DisplayName("Удаление пользователей")
    void clear_Users() {
        User user = getUser();
        userRepository.addUser(user);
        assertTrue(userRepository.clearUsers());
    }

    @Test
    @DisplayName("Добавление задачи")
    void add_Task() {
        User user = getUser();
        userRepository.addUser(user);
        Task task = getTask();
        assertTrue(userRepository.addTask(task));
    }

    @Test
    @DisplayName("Возвращает задачу")
    void get_Task() {
        User user = getUser();
        userRepository.addUser(user);
        Task task = getTask();
        userRepository.addTask(task);
        assertEquals(task, userRepository.getTask(task.getIdUser(), task.getId()));
    }

    @Test
    @DisplayName("Возвращает задачи")
    void get_Tasks() {
        User user = getUser();
        userRepository.addUser(user);
        Task task = getTask();
        userRepository.addTask(task);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        assertEquals(tasks, userRepository.getTasks(task.getIdUser()));
    }

    @Test
    @DisplayName("Изменение задачи")
    void change_Task() {
        User user = getUser();
        userRepository.addUser(user);
        Task task = getTask();
        userRepository.addTask(task);
        task.setStatus("В работе");
        assertTrue(userRepository.changeTask(task));
    }

    @Test
    @DisplayName("Удаление задачи")
    void remove_Task() {
        User user = getUser();
        userRepository.addUser(user);
        Task task = getTask();
        userRepository.addTask(task);
        assertTrue(userRepository.removeTask(user.getId(), task.getId()));
    }

    @Test
    @DisplayName("Удаление задач")
    void clear_Tasks() {
        User user = getUser();
        userRepository.addUser(user);
        Task task = getTask();
        userRepository.addTask(task);
        assertTrue(userRepository.clearTasks());
    }

    private Task getTask() {
        Task task = new Task();
        task.setId(1);
        task.setIdUser(1);
        task.setHeader("Поесть");
        task.setDescription("Придумать что есть");
        task.setDate("21.06.2022");
        task.setStatus("Новая");
        return task;
    }

    private User getUser() {
        User user = new User();
        user.setId(1);
        user.setName("Иван");
        HashMap<Integer,Task> tasks = new HashMap<>();
        Task task = getTask();
        tasks.put(task.getId(), task);
        user.setTasks(tasks);
        return user;
    }
}