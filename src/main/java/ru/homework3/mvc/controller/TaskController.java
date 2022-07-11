package ru.homework3.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import ru.homework3.mvc.dto.TaskDto;
import ru.homework3.mvc.model.Task;
import ru.homework3.mvc.service.TaskService;
import ru.homework3.mvc.utils.TaskMapping;

@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final int NEW_TASK = 1;
    private final int TASK_AT_WORK = 2;
    private final int TASK_DONE = 3;

    @GetMapping("/{id}")
    public String showUsers(@PathVariable("id") Integer idUser,
                            @RequestParam("filter") Integer filter) {
        String tasks = "Задачи пользователя:<br/>" +
                taskService.getTasks(idUser, filter) +
                "<br/>Меню:" +
                "<br/>1. <a href='/" + idUser + "?filter=1'>Отфильтровать задачи по 'ID'.</a>" +
                "<br/>2. <a href='/" + idUser + "?filter=2'>Отфильтровать задачи по 'статусу'.</a>" +
                "<br/>3. <a href='/" + idUser + "/new_task'>Добавить задачу.</a>" +
                "<br/><br/><a href='/'>Главная.</a>";
        return tasks;
    }

    @GetMapping("/{id}/new_task")
    public String newTask(@PathVariable("id") Integer idUser,
                          @RequestParam(value = "error", required = false) String error) {
        String message = error;
        if(message == null) {
            message = "";
        }
        String html = "<form>" +
                "<label for='header'>Введите заголовок задачи: </label>" +
                "<input type='text' id='header'><br/><br/>" +
                "<label for='description'>Введите описание задачи: </label>" +
                "<input type='text' id='description'><br/><br/>" +
                "<label for='date'>Введите дату в формате {dd.MM.yyyy}: </label>" +
                "<input type='text' id='date'><br/><br/>" +
                "</form>" +
                "<button onclick='getFormValue()'>Создать задачу!</button><br/>" +
                "</br><a href='/'>Главная</a>" +
                "</br><a href='/" + idUser + "?filter=1'>Вернуться назад</a>" +
                "<br/><p id='message' style='color:red;'></p>" +
                "<script type='text/javascript'>" +
                "var tagMessage = document.getElementById('message');" +
                "tagMessage.textContent = '" + message + "';" +
                "function getFormValue() {" +
                "const header = document.getElementById('header').value;" +
                "const description = document.getElementById('description').value;" +
                "const date = document.getElementById('date').value;" +
                "if (header === '') {" +
                "tagMessage.textContent = 'Поле {Заголовок} не должно быть пустым';}" +
                "else if (description === '') {" +
                "tagMessage.textContent = 'Поле {Описание} не должно быть пустым';}" +
                "else if (date === '') {" +
                "tagMessage.textContent = 'Поле {Дата} не должно быть пустым';}" +
                "else {document.location.href = '/" + idUser +
                "/newTask?header=' + header + '&description=' + description + '&date=' + date;}}" +
                "</script>";
        return html;
    }

    @GetMapping("/{id}/newTask")
    public RedirectView createTask(@PathVariable("id") Integer idUser,
                                   @RequestParam("header") String header,
                                   @RequestParam("description") String description,
                                   @RequestParam("date") String date) {
        //вместо этого действия вроде желательно принимать dto
        //имитируем приём dto
        TaskDto taskDto = new TaskDto();
        taskDto.setHeader(header);
        taskDto.setDescription(description);
        taskDto.setDate(date);

        ResponseCode code = taskService.createTask(TaskMapping.mapToTask(taskDto, idUser, false));
        RedirectView redirect;
        switch (code) {
            case ERROR_WRITE_OR_READ_CSV -> redirect = new RedirectView("/error_message?message=Error%20writing%20to%20the%20file!");
            case ERROR_VALIDATE -> redirect = new RedirectView("/" + idUser + "/new_task?error=Incorrect%20format%20of%20the%20\"Date\".");
            default -> redirect = new RedirectView("/" + idUser + "?filter=1");
        }
        return redirect;
    }

    @GetMapping("/{id}/update_task/{idTask}")
    public String changeTask(@PathVariable("id") Integer idUser,
                             @PathVariable("idTask") Integer idTask,
                             @RequestParam(value = "error", required = false) String error) {
        String message = error;
        if(message == null) {
            message = "";
        }
        Task task = taskService.getTask(idUser, idTask);
        String status = task.getStatus();
        int valueOption = NEW_TASK;
        if (status.compareTo("В работе") == 0) {
            valueOption = TASK_AT_WORK;
        } else if (status.compareTo("Готово") == 0) {
            valueOption = TASK_DONE;
        }
        String html = "<p>Задача: " + task.getHeader() + "<br/><br/>" +
                "<form>" +
                "<label for='description'>Введите описание задачи: </label>" +
                "<input type='text' id='description'><br/><br/>" +
                "<label for='date'>Введите дату в формате {dd.MM.yyyy}: </label>" +
                "<input type='text' id='date'><br/><br/>" +
                "<label for='status'>Выберите статус: </label>" +
                "<select id='status'><br/><br/>" +
                "<option value='1'>Новая</option>" +
                "<option value='2'>В работе</option>" +
                "<option value='3'>Готово</option>" +
                "</select></form>" +
                "<button onclick='getFormValue()'>Обновить задачу!</button><br/>" +
                "</br><a href='/'>Главная</a>" +
                "</br><a href='/" + idUser + "?filter=1'>Вернуться назад</a>" +
                "<br/><p id='message' style='color:red;'></p>" +
                "<script type='text/javascript'>" +
                "var tagMessage = document.getElementById('message');" +
                "tagMessage.textContent = '" + message + "';" +
                "var tagStatus = document.getElementById('status');" +
                "var tagDescription = document.getElementById('description');" +
                "var tagDate = document.getElementById('date');" +
                "tagDescription.value = '" + task.getDescription() + "';" +
                "tagDate.value = '" + task.getDate() + "';" +
                "tagStatus.value = " + valueOption + ";" +
                "function getFormValue() {" +
                "const status = tagStatus.options[tagStatus.selectedIndex].text;" +
                "const description = tagDescription.value;" +
                "const date = tagDate.value;" +
                "if (description === '') {" +
                "tagMessage.textContent = 'Поле {Описание} не должно быть пустым';}" +
                "else if (date === '') {" +
                "tagMessage.textContent = 'Поле {Дата} не должно быть пустым';}" +
                "else { document.location.href = '/" + idUser +
                "/updateTask/" + idTask + "?header=" + task.getHeader() +
                "&status=' + status + '&description=' + description + '&date=' + date;}}" +
                "</script>";
        return html;
    }

    @GetMapping("/{id}/updateTask/{idTask}")
    public RedirectView updateTask(@PathVariable("id") Integer idUser,
                                   @PathVariable("idTask") Integer idTask,
                                   @RequestParam("header") String header,
                                   @RequestParam("status") String status,
                                   @RequestParam("description") String description,
                                   @RequestParam("date") String date) {
        //вместо этого действия вроде желательно принимать dto
        //имитируем приём dto
        TaskDto taskDto = new TaskDto();
        taskDto.setId(idTask);
        taskDto.setHeader(header);
        taskDto.setDescription(description);
        taskDto.setDate(date);
        taskDto.setStatus(status);

        ResponseCode code = taskService.updateTask(TaskMapping.mapToTask(taskDto, idUser, true));
        RedirectView redirect;
        switch (code) {
            case ERROR_WRITE_OR_READ_CSV -> redirect = new RedirectView("/error_message?message=Error%20writing%20to%20the%20file!");
            case ERROR_VALIDATE -> redirect = new RedirectView("/" + idUser + "/new_task?error=Incorrect%20format%20of%20the%20\"Date\".");
            default -> redirect = new RedirectView("/" + idUser + "?filter=1");
        }
        return redirect;
    }

    @GetMapping("/{id}/delete_task/{idTask}")
    public RedirectView deleteTask(@PathVariable("id") Integer idUser,
                                   @PathVariable("idTask") Integer idTask,
                                   @RequestParam("filter") Integer filter) {
        if (taskService.deleteTask(idUser, idTask) == ResponseCode.SUCCESS) {
            return new RedirectView("/" + idUser + "?filter=" + filter);
        } else {
            return new RedirectView("/error_message?message=Error%20writing%20to%20the%20file!");
        }
    }

    @GetMapping("/delete_all_tasks")
    public RedirectView deleteTasks() {
        if (taskService.deleteTasks() == ResponseCode.SUCCESS) {
            return new RedirectView("/");
        } else {
            return new RedirectView("/error_message?message=Error%20writing%20to%20the%20file!");
        }
    }
}
