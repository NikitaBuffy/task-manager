package ru.pominov.taskmanager.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.pominov.taskmanager.dto.user.UpdateUserDto;
import ru.pominov.taskmanager.dto.user.UserDto;
import ru.pominov.taskmanager.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-12T21:09:37+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UpdateUserDto updateUserDto) {
        if ( updateUserDto == null ) {
            return null;
        }

        User user = new User();

        user.setFirstName( updateUserDto.getFirstName() );
        user.setLastName( updateUserDto.getLastName() );

        return user;
    }

    @Override
    public User toUser(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDto.getId() );
        user.setFirstName( userDto.getFirstName() );
        user.setLastName( userDto.getLastName() );

        return user;
    }

    @Override
    public UserDto toUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( user.getId() );
        userDto.setFirstName( user.getFirstName() );
        userDto.setLastName( user.getLastName() );

        return userDto;
    }
}
