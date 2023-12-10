package ru.pominov.taskmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.pominov.taskmanager.dto.user.UpdateUserDto;
import ru.pominov.taskmanager.dto.user.UserDto;
import ru.pominov.taskmanager.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toUser(UpdateUserDto updateUserDto);

    User toUser(UserDto userDto);

    UserDto toUserDto(User user);
}
