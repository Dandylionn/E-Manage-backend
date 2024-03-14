package com.udemy.spring.bootrestfulwebservices.mapper;

import com.udemy.spring.bootrestfulwebservices.dto.UserDto;
import com.udemy.spring.bootrestfulwebservices.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AutoUserMapper {

    AutoUserMapper MAPPER = Mappers.getMapper(AutoUserMapper.class);
    UserDto mapToUserDto(User user);
    User mapToUser(UserDto userDto);
}
