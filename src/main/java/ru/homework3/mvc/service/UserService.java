package ru.homework3.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.homework3.mvc.dto.UserDto;
import ru.homework3.mvc.model.User;
import ru.homework3.mvc.repo.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final String SUCCESS = "Success!";

    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> dtos = new ArrayList<>(users.size());
        for (User user: users) {
            dtos.add(UserDto.build(user));
        }
        return dtos;
    }

    public UserDto getUser(Integer idUser) {
        if (userRepository.existsById(idUser)) {
            return UserDto.build(userRepository.getReferenceById(idUser));
        } else {
            return null;
        }
    }

    public String createUser(UserDto dto) {
        userRepository.save(dto.toUser());
        return SUCCESS;
    }

    public String updateUser(UserDto dto) {
        if (userRepository.existsById(dto.getId())) {
            User user = userRepository.getReferenceById(dto.getId());
            user.setName(dto.getName());
            userRepository.save(user);
            return SUCCESS;
        }
        return null;
    }

    public String deleteUser(Integer idUser) {
        if (userRepository.existsById(idUser)) {
            userRepository.deleteById(idUser);
            return SUCCESS;
        }
        return "Fail!";
    }

    public String deleteUsers() {
        userRepository.deleteAll();
        return SUCCESS;
    }
}
