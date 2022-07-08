package ru.homework3.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.homework3.mvc.model.Task;
import ru.homework3.mvc.model.User;

import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DtoEntities {
    private User user;
    private Task task;
    private Integer filter;

    public void setFilter(Integer filter) {
        if (filter == null) {
            this.filter = 1;
        } else {
            this.filter = filter;
        }
    }

    public void setUser (Integer userId, String name) {
        user = new User();
        user.setName(name);
        user.setId(userId);
    }

    public void setTask(Integer taskId, Integer userId, String header, String description, String date, String status) {
        task = new Task();
        task.setId(taskId);
        task.setIdUser(userId);
        task.setHeader(header);
        task.setDescription(description);
        task.setDate(date);
        task.setStatus(status);
    }
}
