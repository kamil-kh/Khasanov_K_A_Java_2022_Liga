package ru.homework3.mvc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.homework3.mvc.model.Task;
import ru.homework3.mvc.repo.UserRepository;
import ru.homework3.mvc.utils.ResponseCode;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskServiceTest {
    @Mock
    UserRepository userRepository;
    TaskService taskService;

    @BeforeEach
    private void preTest() {
        userRepository = Mockito.mock(UserRepository.class);
        taskService = new TaskService(userRepository);
    }

    @Test
    @DisplayName("Возвращает задачи")
    void get_Tasks() {
        Task task = getTask();
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        Mockito.when(userRepository.getTasks(task.getIdUser())).thenReturn(tasks);
        int filter = 1;
        String taskHtml = task.toString() +
                " {<a href='/" + task.getIdUser() +
                "/update_task/" + task.getId() +
                "'>Изменить</a> | <a href='/" + task.getIdUser() +
                "/delete_task/" + + task.getId() +
                "?filter=" + filter + "'>Удалить</a>}<br/>";
        assertEquals(taskHtml, taskService.getTasks(task.getIdUser(), filter));
    }

    @Test
    @DisplayName("Возвращает задачу")
    void get_Task() {
        Task task = getTask();
        Mockito.when(userRepository.getTask(task.getIdUser(), task.getId())).thenReturn(task);
        assertEquals(task, taskService.getTask(task.getIdUser(), task.getId()));
    }

    @Test
    @DisplayName("Создаёт задачу")
    void create_Task() {
        Task task = getTask();
        Task task2 = getTask();
        task2.setDate("23.33.2023");
        Mockito.when(userRepository.addTask(task)).thenReturn(true);
        assertEquals(ResponseCode.ERROR_VALIDATE, taskService.createTask(task2));
    }

    @Test
    @DisplayName("Удаляет задачу")
    void delete_Task() {
        Mockito.when(userRepository.removeTask(1, 2)).thenReturn(true);
        assertEquals(ResponseCode.SUCCESS, taskService.deleteTask(1, 2));
    }

    @Test
    @DisplayName("Удаляет задачи")
    void delete_Tasks() {
        Mockito.when(userRepository.clearTasks()).thenReturn(true);
        assertEquals(ResponseCode.SUCCESS, taskService.deleteTasks());
    }

    @Test
    @DisplayName("Обновляет задачу")
    void update_Task() {
        Task task = getTask();
        Mockito.when(userRepository.changeTask(task)).thenReturn(true);
        assertEquals(ResponseCode.SUCCESS, taskService.updateTask(task));
    }

    private Task getTask() {
        Task task = new Task();
        task.setId(2);
        task.setIdUser(1);
        task.setHeader("Выполнить ДЗ");
        task.setDescription("Придумать и написать кучу кода");
        task.setDate("27.06.2022");
        task.setStatus("Новая");
        return task;
    }
}