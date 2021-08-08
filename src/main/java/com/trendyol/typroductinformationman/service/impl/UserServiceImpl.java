package com.trendyol.typroductinformationman.service.impl;

import com.trendyol.typroductinformationman.dto.CreateUserDto;
import com.trendyol.typroductinformationman.dto.UserDto;
import com.trendyol.typroductinformationman.model.User;
import com.trendyol.typroductinformationman.repository.UserRepository;
import com.trendyol.typroductinformationman.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String addUser(CreateUserDto createUserDto) {
        User user = new User();
        user.setEmail(createUserDto.getEmail());
        user.setName(createUserDto.getName());

        userRepository.save(user);

        return "user added";
    }

    @Override
    public UserDto getUser(Integer userId) {
        User user = userRepository.findById(userId.longValue()).orElseThrow(EntityNotFoundException::new);
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setUserId(user.getId().intValue());

        return userDto;
    }

    @Override
    public List<UserDto> getUserList(List<Integer> userIdList) {
        List<UserDto> userDtoList = new ArrayList<>();
        userIdList.stream().forEach(x -> {
            UserDto userDto = getUser(x);
            userDtoList.add(userDto);
        });
        return userDtoList;
    }
}
