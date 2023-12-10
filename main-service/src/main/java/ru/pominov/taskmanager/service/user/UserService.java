package ru.pominov.taskmanager.service.user;

import ru.pominov.taskmanager.dto.user.UpdateUserDto;
import ru.pominov.taskmanager.dto.user.UserDto;
import ru.pominov.taskmanager.model.User;

public interface UserService {

    User getExistingUser(Long userId);

    UserDto editUser(Long userId, UpdateUserDto updateUserDto);
}
