package ru.homework3.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.homework3.mvc.model.Task;
import ru.homework3.mvc.service.UserService;
import ru.homework3.mvc.validator.Validator;

//Не делайте так, а именно: не вставляйте здесь html. Это хардкод для лучшего интерфейса на странице.
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/")
    public String index() {
        String users = "Пользователи:<br/>" +
                userService.getUsers() +
                "<br/>Меню:" +
                "<br/>1. <a href='/new_user'>Добавить пользователя.</a>" +
                "<br/>2. <a href='/delete_all_user'>Удалить всех пользователей.</a>" +
                "<br/>3. <a href='/delete_all_tasks'>Удалить все задачи.</a>";
        return users;
    }

    @GetMapping("/{id}")
    public String showUsers(@PathVariable("id") int idUser,
                            @RequestParam("filter") int filter) {
        String tasks = "Задачи пользователя:<br/>" +
                userService.getTasks(idUser, filter) +
                "<br/>Меню:" +
                "<br/>1. <a href='/" + idUser + "?filter=1'>Отфильтровать задачи по 'ID'.</a>" +
                "<br/>2. <a href='/" + idUser + "?filter=2'>Отфильтровать задачи по 'статусу'.</a>" +
                "<br/>3. <a href='/" + idUser + "/new_task'>Добавить задачу.</a>" +
                "<br/><br/><a href='/'>Главная.</a>";
        return tasks;
    }

    @GetMapping("/new_user")
    public String newUser(@RequestParam(value = "error", required = false) String error) {
        String message = error;
        if(message == null) {
            message = "";
        }
        String html = "<form>" +
                "<label for='name'>Введите имя: </label>" +
                "<input type='text' id='name'><br/><br/>" +
                "<span> Пример: Олег. (Имя должно начинаться с большой буквы и не иметь пробелов.)</span>" +
                "</form>" +
                "<button onclick='getFormValue()'>Создать пользователя!</button><br/>" +
                "</br><a href='/'>Главная</a><br/>" +
                "<br/><p id='message' style='color:red;'></p>" +
                "<script type='text/javascript'>" +
                "document.getElementById('message').textContent = '" + message + "';" +
                "function getFormValue() {" +
                "let name = document.getElementById('name').value;" +
                "if (name === '') {" +
                "document.getElementById('message').textContent = 'Поле {Имя} не должно быть пустым';}" +
                "else {document.location.href = '/newUser?name=' + name;}};" +
                "</script>";
        return html;
    }

    @GetMapping("/newUser")
    public RedirectView createUser(@RequestParam("name") String name) {
        if (Validator.validateName(name)) {
            if (userService.createUser(name)) {
                return new RedirectView("/");
            } else {
                return new RedirectView("/error_message?message=Error%20writing%20to%20the%20file!");
            }
        } else {
            return new RedirectView("/new_user?error=Incorrect%20format%20of%20the%20\"Name\".");
        }

    }

    @GetMapping("/{id}/new_task")
    public String newTask(@PathVariable("id") int idUser,
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
    public RedirectView createTask(@PathVariable("id") int idUser,
                                   @RequestParam("header") String header,
                                   @RequestParam("description") String description,
                                   @RequestParam("date") String date) {
        if (!Validator.validateDate(date)) {
            return new RedirectView("/" + idUser + "/new_task?error=Incorrect%20format%20of%20the%20\"Date\".");
        } else {
            if (userService.createTask(idUser, header, description, date)) {
                return new RedirectView("/" + idUser + "?filter=1");
            } else {
                return new RedirectView("/error_message?message=Error%20writing%20to%20the%20file!");
            }
        }
    }

    @GetMapping("/{id}/update_task/{idTask}")
    public String changeTask(@PathVariable("id") int idUser,
                             @PathVariable("idTask") int idTask,
                             @RequestParam(value = "error", required = false) String error) {
        String message = error;
        if(message == null) {
            message = "";
        }
        Task task = userService.getTask(idUser, idTask);
        String status = task.getStatus();
        int value = 1;
        if (status.compareTo("В работе") == 0) {
            value = 2;
        } else if (status.compareTo("Готово") == 0) {
            value = 3;
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
                "tagStatus.value = " + value + ";" +
                "function getFormValue() {" +
                "const status = tagStatus.options[tagStatus.selectedIndex].text;" +
                "const description = tagDescription.value;" +
                "const date = tagDate.value;" +
                "if (description === '') {" +
                "tagMessage.textContent = 'Поле {Описание} не должно быть пустым';}" +
                "else if (date === '') {" +
                "tagMessage.textContent = 'Поле {Дата} не должно быть пустым';}" +
                "else { document.location.href = '/" + idUser +
                "/updateTask/" + idTask +
                "?status=' + status + '&description=' + description + '&date=' + date;}}" +
                "</script>";
        return html;
    }

    @GetMapping("/{id}/updateTask/{idTask}")
    public RedirectView updateTask(@PathVariable("id") int idUser,
                                   @PathVariable("idTask") int idTask,
                                   @RequestParam("status") String status,
                                   @RequestParam("description") String description,
                                   @RequestParam("date") String date) {
        if (!Validator.validateDate(date)) {
            return new RedirectView("/" + idUser + "/update_task/" +
                    idTask + "?error=Incorrect%20format%20of%20the%20\"Date\"."
            );
        } else {
            if (userService.updateTask(idUser, idTask, description, date, status)) {
                return new RedirectView("/" + idUser + "?filter=1");
            } else {
                return new RedirectView("/error_message?message=Error%20writing%20to%20the%20file!");
            }
        }
    }

    @GetMapping("/{id}/delete_task/{idTask}")
    public RedirectView deleteTask(@PathVariable("id") int idUser,
                                   @PathVariable("idTask") int idTask,
                                   @RequestParam("filter") int filter) {
        if (userService.deleteTask(idUser, idTask)) {
            return new RedirectView("/" + idUser + "?filter=" + filter);
        } else {
            return new RedirectView("/error_message?message=Error%20writing%20to%20the%20file!");
        }
    }

    @GetMapping("/{id}/delete_user")
    public RedirectView deleteUser(@PathVariable("id") int idUser) {
        if (userService.deleteUser(idUser)) {
            return new RedirectView("/");
        } else {
            return new RedirectView("/error_message?message=Error%20writing%20to%20the%20file!");
        }
    }

    @GetMapping("/delete_all_user")
    public RedirectView deleteUsers() {
        if (userService.deleteUsers()) {
            return new RedirectView("/");
        } else {
            return new RedirectView("/error_message?message=Error%20writing%20to%20the%20file!");
        }
    }

    @GetMapping("/delete_all_tasks")
    public RedirectView deleteTasks() {
        if (userService.deleteTasks()) {
            return new RedirectView("/");
        } else {
            return new RedirectView("/error_message?message=Error%20writing%20to%20the%20file!");
        }
    }

    @GetMapping("/error_message")
    public String error(@RequestParam("message") String message) {
        return message + "<br/><a href='/'>Главная</a>";
    }
}
