package ru.homework3.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.homework3.mvc.dto.UserDto;
import ru.homework3.mvc.service.UserService;
import ru.homework3.mvc.utils.UserMapping;

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
        //вместо этого действия вроде желательно принимать dto
        //имитируем приём dto
        UserDto userDto = new UserDto();
        userDto.setName(name);

        ResponseCode code = userService.createUser(UserMapping.mapToUser(userDto, false));
        RedirectView redirect;
        if (code == ResponseCode.ERROR_VALIDATE) {
            redirect = new RedirectView("/new_user?error=Incorrect%20format%20of%20the%20\"Name\".");
        } else {
            redirect = new RedirectView("/");
        }
        return redirect;
    }

    @GetMapping("/{id}/delete_user")
    public RedirectView deleteUser(@PathVariable("id") Integer idUser) {
        userService.deleteUser(idUser);
        return new RedirectView("/");
    }

    @GetMapping("/delete_all_user")
    public RedirectView deleteUsers() {
        userService.deleteUsers();
        return new RedirectView("/");
    }
}
