package com.trendyol.typroductinformationman.service;

import com.trendyol.typroductinformationman.dto.CreateUserDto;
import com.trendyol.typroductinformationman.dto.UserDto;

import java.util.List;

public interface UserService {
    String addUser(CreateUserDto createUserDto);

    UserDto getUser(Integer userId);

    List<UserDto> getUserList (List<Integer> userIdList);
}
