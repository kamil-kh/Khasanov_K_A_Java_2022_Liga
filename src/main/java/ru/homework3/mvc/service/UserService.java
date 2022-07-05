package ru.homework3.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.homework3.mvc.model.User;
import ru.homework3.mvc.repo.UserRepository;
import ru.homework3.mvc.utils.ResponseCode;
import ru.homework3.mvc.validator.Validator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public String getUsers() {
        String usersStr = "";
        List<User> users = userRepository.getUsers();
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
            return getCode(userRepository.addUser(user));
        } else {
            return ResponseCode.ERROR_VALIDATE;
        }
    }

    public ResponseCode deleteUser(int idUser) {
        return getCode(userRepository.removeUser(idUser));
    }

    public ResponseCode deleteUsers() {
        return getCode(userRepository.clearUsers());
    }

    private ResponseCode getCode(boolean isSuccess) {
        if (isSuccess) {
            return ResponseCode.SUCCESS;
        } else {
            return ResponseCode.ERROR_WRITE_OR_READ_CSV;
        }
    }
}
