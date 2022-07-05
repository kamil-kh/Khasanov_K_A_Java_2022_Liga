package ru.homework3.mvc.utils;

import org.springframework.stereotype.Service;
import ru.homework3.mvc.dto.TaskDto;
import ru.homework3.mvc.model.Task;

@Service
public class TaskMapping {

    public TaskDto mapToTaskDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setHeader(task.getHeader());
        taskDto.setDescription(task.getDescription());
        taskDto.setDate(task.getDate());
        taskDto.setStatus(task.getStatus());
        return taskDto;
    }

    public Task mapToTask(TaskDto taskDto, int idUser, boolean withId) {
        Task task = new Task();
        if (withId) {
            task.setId(taskDto.getId());
        }
        task.setIdUser(idUser);
        task.setHeader(taskDto.getHeader());
        task.setDescription(taskDto.getDescription());
        task.setDate(taskDto.getDate());
        task.setStatus(taskDto.getStatus());
        return task;
    }
}
