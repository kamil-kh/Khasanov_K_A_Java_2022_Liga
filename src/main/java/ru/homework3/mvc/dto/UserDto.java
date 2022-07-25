package ru.homework3.mvc.dto;

import lombok.*;
import ru.homework3.mvc.dto.transfer.New;
import ru.homework3.mvc.dto.transfer.Update;
import ru.homework3.mvc.model.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @Null(groups = {New.class, Update.class})
    private Integer id;
    @NotNull(groups = {New.class, Update.class})
    private String name;

    public User toUser() {
        User user = new User();
        user.setId(id);
        user.setName(name);
        return user;
    }

    static public UserDto build(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        return dto;
    }
}
