package com.IMS.IssueManagementSystem.Controller;


import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.IMS.IssueManagementSystem.DTO.UserDtos;
import com.IMS.IssueManagementSystem.Model.Role;
import com.IMS.IssueManagementSystem.Model.User;
import com.IMS.IssueManagementSystem.Service.RoleService;
import com.IMS.IssueManagementSystem.Service.UserService;

@RestController
@RequestMapping(value="/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/register")
    public ResponseEntity<UserDtos.RegisterResponse> registerUser(
            @RequestBody UserDtos.RegisterRequest request) {

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setContact(request.getContact());

        Role defaultRole = roleService.getRoleByName("USER");


        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);


        user.setRoles(roles);


        User savedUser = userService.registerUser(user);

        List<String> roleNames = savedUser.getRoles()
                .stream()
                .map(Role::getName)
                .toList();

        UserDtos.RegisterResponse.Data data = new UserDtos.RegisterResponse.Data(
        savedUser.getId(),
        savedUser.getUsername(),
        savedUser.getEmail(),
        roleNames,
        savedUser.getContact()
        );


        UserDtos.RegisterResponse response = new UserDtos.RegisterResponse(
        200,
        "User registered successfully",
        "success",
        new Date(),
        data
        );


        return ResponseEntity.ok(response);
    }




    @PostMapping("/login")
    public ResponseEntity<UserDtos.LoginResponse> login(@RequestBody UserDtos.LoginRequest request) {
        User loggedInUser = userService.loginUser(request.getUsername(), request.getPassword());

        List<String> roleNames = loggedInUser.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        UserDtos.LoginResponse.Data data = new UserDtos.LoginResponse.Data(
                loggedInUser.getId(),
                loggedInUser.getUsername(),
                loggedInUser.getEmail(),
                roleNames,
                loggedInUser.getContact()
        );

        UserDtos.LoginResponse response = new UserDtos.LoginResponse(
                200,
                "Login successful",
                "success",
                new Date(),
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

