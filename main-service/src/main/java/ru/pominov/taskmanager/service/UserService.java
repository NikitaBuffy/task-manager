package ru.pominov.taskmanager.service;

import ru.pominov.taskmanager.model.User;

public interface UserService {

    User getExistingUser(Long userId);
}
