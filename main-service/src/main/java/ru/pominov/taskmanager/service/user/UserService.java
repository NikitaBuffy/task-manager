package ru.pominov.taskmanager.service.user;

import ru.pominov.taskmanager.model.User;

public interface UserService {

    User getExistingUser(Long userId);
}
