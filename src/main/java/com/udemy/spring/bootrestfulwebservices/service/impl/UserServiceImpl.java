package com.udemy.spring.bootrestfulwebservices.service.impl;

import com.udemy.spring.bootrestfulwebservices.dto.UserDto;
import com.udemy.spring.bootrestfulwebservices.entity.User;
import com.udemy.spring.bootrestfulwebservices.exception.EmailAlreadyExistsException;
import com.udemy.spring.bootrestfulwebservices.exception.ResourceNotFoundException;
import com.udemy.spring.bootrestfulwebservices.mapper.AutoUserMapper;
import com.udemy.spring.bootrestfulwebservices.repository.UserRepository;
import com.udemy.spring.bootrestfulwebservices.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private ModelMapper modelMapper;
    @Override
    public UserDto createUser(UserDto userDto) {

        //Convert UserDto into User JPA Entity
        //User user = UserMapper.mapToUser(userDto);
//        User user = modelMapper.map(userDto, User.class); //ORIGINAL MAPPING METHOD

        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
        if(optionalUser.isPresent()){
            throw new EmailAlreadyExistsException("Email Already Exists for User");
        }

        //Mapstruct method
        User user = AutoUserMapper.MAPPER.mapToUser(userDto);

        User savedUser = userRepository.save(user);

        //Convert User JPA entity to UserDto
//        UserDto savedUserDto = UserMapper.mapToUserDto(savedUser);
//        UserDto savedUserDto = modelMapper.map(savedUser, UserDto.class); //ORI MAPPING METHOD

        //Mapstruct method
        UserDto savedUserDto = AutoUserMapper.MAPPER.mapToUserDto(savedUser);


        return savedUserDto;
    }

    @Override
    public UserDto getUserById(Long userId) {
//        Optional<User> optionalUser = userRepository.findById(userId); //w/o catching exception
//        User user = optionalUser.get();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId)
        );

//        return UserMapper.mapToUserDto(user);

        //original mapping method
//        return modelMapper.map(user, UserDto.class);

        //Mapstruct method
//        return AutoUserMapper.MAPPER.mapToUserDto(optionalUser.get());
        return AutoUserMapper.MAPPER.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
//        return users.stream().map(UserMapper::mapToUserDto)
//                .collect(Collectors.toList());

        //ori mapping method
//        return users.stream().map((user) -> modelMapper.map(user, UserDto.class))
//                .collect(Collectors.toList());

        //Mapstruct method
        return users.stream().map((user) -> AutoUserMapper.MAPPER.mapToUserDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(UserDto user) {
        User existingUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", user.getId())
        );
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        User updatedUser = userRepository.save(existingUser);
//        return UserMapper.mapToUserDto(updatedUser);

        //Ori mapping method
//        return modelMapper.map(updatedUser, UserDto.class);

        //Mapstruct method
        return AutoUserMapper.MAPPER.mapToUserDto(updatedUser);
    }


    @Override
    public void deleteUser(Long userId) {
        User existingUser = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId)
        );
        userRepository.deleteById(userId);
    }


}
