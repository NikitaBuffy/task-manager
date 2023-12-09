package ru.pominov.taskmanager.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.pominov.taskmanager.security.dto.UserInfoRequestDto;
import ru.pominov.taskmanager.security.dto.UserInfoResponseDto;
import ru.pominov.taskmanager.security.model.UserInfo;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserInfoMapper {

    UserInfo userRequestDtoToUserInfo(UserInfoRequestDto userDto);

    UserInfoResponseDto userInfoToUserResponseDto(UserInfo user);
}
