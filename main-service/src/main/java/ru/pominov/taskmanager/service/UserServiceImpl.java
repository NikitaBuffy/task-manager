package ru.pominov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pominov.taskmanager.exceptions.DataNotFoundException;
import ru.pominov.taskmanager.model.User;
import ru.pominov.taskmanager.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getExistingUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new DataNotFoundException(String.format("User with id=%d was not found", userId));
        });
    }
}
