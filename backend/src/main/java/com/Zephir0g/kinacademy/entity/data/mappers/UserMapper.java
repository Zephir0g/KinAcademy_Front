package com.Zephir0g.kinacademy.entity.data.mappers;

import com.Zephir0g.kinacademy.dto.user.SingUpUserDto;
import com.Zephir0g.kinacademy.dto.user.UserDto;
import com.Zephir0g.kinacademy.entity.course.Course;
import com.Zephir0g.kinacademy.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserDto userToDto(User user);

    @Mapping(target = "password", ignore = true)
    User singUpDtoToUser(SingUpUserDto singUpDto);

    User userDtoToUser(UserDto userDto);

    default UserDto userToUserDto(User user) {
        UserDto userDto = userToDto(user);
        userDto.setCoursesId(user.getCoursesId());
        return userDto;
    }


}
