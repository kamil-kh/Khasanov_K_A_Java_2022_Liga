package ru.homework3.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.homework3.mvc.controller.ResponseCode;
import ru.homework3.mvc.model.User;
import ru.homework3.mvc.repo.UserRepository;
import ru.homework3.mvc.utils.validator.Validator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public String getUsers() {
        String usersStr = "";
        List<User> users = userRepository.findAll();
        for (User user: users) {
            usersStr += user.toString() +
                    " {<a href='/" +
                    user.getId() +
                    "?filter=1'>Вывести задачи</a> | <a href='" +
                    user.getId() +
                    "/delete_user'>Удалить</a>}<br/>";
        }
        return usersStr;
    }

    public ResponseCode createUser(User user) {
        if (Validator.validateName(user.getName())) {
            userRepository.save(user);
            return ResponseCode.SUCCESS;
        } else {
            return ResponseCode.ERROR_VALIDATE;
        }
    }

    public void deleteUser(Integer idUser) {
        userRepository.deleteById(idUser);
    }

    public void deleteUsers() {
        userRepository.deleteAll();
    }
}
