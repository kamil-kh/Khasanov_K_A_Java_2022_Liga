package ru.homework3.mvc.utils;

import org.springframework.stereotype.Service;
import ru.homework3.mvc.dto.UserDto;
import ru.homework3.mvc.model.User;

import java.util.HashMap;

@Service
public class UserMapping {

    public UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        return userDto;
    }

    public User mapToUser(UserDto userDto, boolean withId) {
        User user = new User();
        if (withId) {
            user.setId(userDto.getId());
        }
        user.setName(userDto.getName());
        user.setTasks(new HashMap<>());
        return user;
    }
}
