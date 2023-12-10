package ru.pominov.taskmanager.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pominov.taskmanager.dto.user.UpdateUserDto;
import ru.pominov.taskmanager.dto.user.UserDto;
import ru.pominov.taskmanager.exceptions.DataNotFoundException;
import ru.pominov.taskmanager.mapper.UserMapper;
import ru.pominov.taskmanager.model.User;
import ru.pominov.taskmanager.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto editUser(Long userId, UpdateUserDto updateUserDto) {
        User user = getExistingUser(userId);
        if (updateUserDto.getFirstName() != null) {
            user.setFirstName(updateUserDto.getFirstName());
        }
        if (updateUserDto.getLastName() != null) {
            user.setLastName(updateUserDto.getLastName());
        }

        User updatedUser = userRepository.save(user);
        log.info("Update user with ID: {}. New data = {}", userId, updatedUser);
        return userMapper.toUserDto(updatedUser);
    }

    @Override
    public User getExistingUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new DataNotFoundException(String.format("User with id=%d was not found", userId));
        });
    }
}
