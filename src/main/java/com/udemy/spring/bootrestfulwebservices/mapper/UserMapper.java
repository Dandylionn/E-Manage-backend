package com.udemy.spring.bootrestfulwebservices.mapper;

import com.udemy.spring.bootrestfulwebservices.dto.UserDto;
import com.udemy.spring.bootrestfulwebservices.entity.User;

public class UserMapper {
    public static UserDto mapToUserDto(User user){
        //Convert User JPA entity into UserDto
        UserDto userDto = new UserDto(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail()
        );
        return userDto;
    }

    //Convert UserDto into User JPA Entity
    public static User mapToUser(UserDto userDto){
        User user = new User(
                userDto.getId(),
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getEmail()
        );
        return user;
    }
}
