package ru.pominov.taskmanager.security.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.pominov.taskmanager.security.dto.UserInfoRequestDto;
import ru.pominov.taskmanager.security.dto.UserInfoResponseDto;
import ru.pominov.taskmanager.security.model.UserInfo;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-12T21:09:37+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class UserInfoMapperImpl implements UserInfoMapper {

    @Override
    public UserInfo userRequestDtoToUserInfo(UserInfoRequestDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserInfo userInfo = new UserInfo();

        userInfo.setEmail( userDto.getEmail() );
        userInfo.setPassword( userDto.getPassword() );

        return userInfo;
    }

    @Override
    public UserInfoResponseDto userInfoToUserResponseDto(UserInfo user) {
        if ( user == null ) {
            return null;
        }

        UserInfoResponseDto.UserInfoResponseDtoBuilder userInfoResponseDto = UserInfoResponseDto.builder();

        userInfoResponseDto.id( user.getId() );
        userInfoResponseDto.email( user.getEmail() );
        userInfoResponseDto.password( user.getPassword() );
        userInfoResponseDto.roles( user.getRoles() );

        return userInfoResponseDto.build();
    }
}
