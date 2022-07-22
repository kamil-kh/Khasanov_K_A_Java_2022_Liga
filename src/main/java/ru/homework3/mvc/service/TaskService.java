package ru.homework3.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.homework3.mvc.dto.TaskDto;
import ru.homework3.mvc.model.Task;
import ru.homework3.mvc.model.User;
import ru.homework3.mvc.repo.TaskRepository;
import ru.homework3.mvc.repo.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public List<TaskDto> getTasks(Integer idUser) {
        if (userRepository.existsById(idUser)) {
            List<Task> tasks = taskRepository.findAll(TaskDto.hasTasksWithIdUser(idUser));
            List<TaskDto> dtos = new ArrayList<>(tasks.size());
            for (Task task : tasks) {
                dtos.add(TaskDto.build(task));
            }
            return dtos;
        }
        return null;
    }

    public TaskDto getTask(Integer idUser, Integer idTask) {
        if (userRepository.existsById(idUser) && taskRepository.existsById(idTask)) {
            List<Task> tasks = taskRepository.findAll(TaskDto.hasTaskWithIdUser(idUser, idTask));
            if(tasks.size() == 1) {
                if(tasks.get(0).getId() == idTask) {
                    return TaskDto.build(taskRepository.getReferenceById(idTask));
                }
            }
        }
        return null;
    }

    public TaskDto createTask(TaskDto dto, Integer idUser) {
        if (userRepository.existsById(idUser)) {
            Task task = dto.toTask();
            task.setUser(userRepository.getReferenceById(idUser));
            return TaskDto.build(taskRepository.save(task));
        }
        return null;
    }

    public TaskDto updateTask(TaskDto dto, Integer idUser) {
        if (userRepository.existsById(idUser) && taskRepository.existsById(dto.getId())) {
            List<Task> tasks = taskRepository.findAll(TaskDto.hasTaskWithIdUser(idUser, dto.getId()));
            if(tasks.size() == 1) {
                Task task = tasks.get(0);
                task.setDescription(dto.getDescription());
                task.setDate(dto.getDate());
                task.setStatus(dto.getStatus());
                return TaskDto.build(taskRepository.save(task));
            }
        }
        return null;
    }

    public String deleteTask(Integer idUser, Integer idTask) {
        if (userRepository.existsById(idUser) && taskRepository.existsById(idTask)) {
            List<Task> tasks = taskRepository.findAll(TaskDto.hasTaskWithIdUser(idUser, idTask));
            if(tasks.size() == 1) {
                if(tasks.get(0).getId() == idTask) {
                    taskRepository.deleteById(idTask);
                    return "Done!";
                }
            }
        }
        return "Fail!";
    }

    public void deleteTasks(Integer idUser) {
        if (userRepository.existsById(idUser)) {
            User user = userRepository.getReferenceById(idUser);
            user.clearTasks();
            userRepository.save(user);
        }
    }
}
