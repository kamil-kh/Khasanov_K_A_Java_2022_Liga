package ru.homework3.mvc.utils;

import ru.homework3.mvc.dto.UserDto;
import ru.homework3.mvc.model.Task;
import ru.homework3.mvc.model.User;

import java.util.*;

public class UserMapping {

    private UserMapping(){}

    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        return userDto;
    }

    public static User mapToUser(UserDto userDto, boolean withId) {
        User user = new User();
        if (withId) {
            user.setId(userDto.getId());
        }
        user.setName(userDto.getName());
        return user;
    }
}
