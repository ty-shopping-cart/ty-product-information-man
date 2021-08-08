package com.trendyol.typroductinformationman.controller;


import com.trendyol.typroductinformationman.dto.*;
import com.trendyol.typroductinformationman.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<UserDto> getUser (Integer userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PostMapping("/getUserList")
    public ResponseEntity<List<UserDto>> getUser (@RequestBody List<Integer> userIdList) {
        return ResponseEntity.ok(userService.getUserList(userIdList));
    }

    @PostMapping("/")
    public ResponseEntity<String> createProduct (@RequestBody CreateUserDto createUserDto) {
        return ResponseEntity.ok(userService.addUser(createUserDto));
    }

}
