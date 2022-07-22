package ru.homework3.mvc.dto;

import lombok.*;
import org.springframework.data.jpa.domain.Specification;
import ru.homework3.mvc.dto.transfer.New;
import ru.homework3.mvc.dto.transfer.Update;
import ru.homework3.mvc.model.Task;
import ru.homework3.mvc.model.metamodel.Task_;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    @Null(groups = {New.class, Update.class})
    private Integer id;
    @NotNull(groups = {New.class})
    @Null(groups ={Update.class})
    private String header;
    @NotNull(groups = {New.class, Update.class})
    private String description;
    @NotNull(groups = {New.class, Update.class})
    @Pattern(regexp = "(0?[1-9]|[12][0-9]|3[01]).(0?[1-9]|1[012]).((19|20)\\d\\d)", groups = {New.class, Update.class})
    private String date;
    @Null(groups = {New.class})
    @NotNull(groups = {Update.class})
    @Pattern(regexp = "Новая|В работе|Готово", groups = {Update.class})
    private String status;

    public Task toTask() {
        Task task = new Task();
        task.setId(id);
        task.setHeader(header);
        task.setDescription(description);
        task.setDate(date);
        if (status == null) {
            status = "Новая";
        }
        task.setStatus(status);
        return task;
    }

    static public TaskDto build(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setHeader(task.getHeader());
        dto.setDescription(task.getDescription());
        dto.setDate(task.getDate());
        dto.setStatus(task.getStatus());
        return dto;
    }

    public static Specification<Task> hasTasksWithIdUser(Integer idUser) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Task_.USER), idUser);
    }

    public static Specification<Task> hasTaskWithIdUser(Integer idUser, Integer idTask) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get(Task_.USER), idUser),
                criteriaBuilder.equal(root.get(Task_.ID), idTask)
        );
    }
}
