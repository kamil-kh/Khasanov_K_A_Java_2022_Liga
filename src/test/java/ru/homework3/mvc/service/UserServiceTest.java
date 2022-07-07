package ru.homework3.mvc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.homework3.mvc.controller.ResponseCode;
import ru.homework3.mvc.model.User;
import ru.homework3.mvc.repo.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserServiceTest {

    UserService userService;
    @Mock
    UserRepository userRepository;

    @BeforeEach
    private void preTest() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Возвращает пользователей")
    void get_Users() {
        List<User> users = new ArrayList<>();
        User user = new User(1, "Олег", new HashMap<>());
        users.add(user);
        Mockito.when(userRepository.getUsers()).thenReturn(users);
        String userHtml = user.toString() +
                " {<a href='/" +
                user.getId() +
                "?filter=1'>Вывести задачи</a> | <a href='" +
                user.getId() +
                "/delete_user'>Удалить</a>}<br/>";
        assertEquals(userHtml, userService.getUsers());
    }

    @Test
    @DisplayName("Создаёт пользователя")
    void create_User() {
        User user = getUser();
        User user2 = new User();
        user2.setName("коля");
        Mockito.when(userRepository.addUser(user)).thenReturn(true);
        assertEquals(ResponseCode.ERROR_VALIDATE, userService.createUser(user2));
    }

    @Test
    @DisplayName("Удаляет пользователя")
    void delete_User() {
        Mockito.when(userRepository.removeUser(1)).thenReturn(true);
        assertEquals(ResponseCode.SUCCESS, userService.deleteUser(1));
    }

    @Test
    @DisplayName("Удаляет пользователей")
    void delete_Users() {
        Mockito.when(userRepository.clearUsers()).thenReturn(true);
        assertEquals(ResponseCode.SUCCESS, userService.deleteUsers());
    }

    private User getUser() {
        User user = new User();
        user.setName("Иван");
        user.setTasks(new HashMap<>());
        return user;
    }
}