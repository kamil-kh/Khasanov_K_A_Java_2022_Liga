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

    public UserDto createUser(UserDto dto) {
        return UserDto.build(userRepository.save(dto.toUser()));
    }

    public String deleteUser(Integer idUser) {
        if (userRepository.existsById(idUser)) {
            userRepository.deleteById(idUser);
            return "Done!";
        }
        return "Fail!";
    }

    public void deleteUsers() {
        userRepository.deleteAll();
    }
}
