package com.qa.automation.controller;

import com.qa.automation.dto.UserDto;
import com.qa.automation.model.User;
import com.qa.automation.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<UserDto> getUser(@RequestParam String userName, @RequestParam String password) {
        log.info("Fetching user details for username: {}", userName);
        UserDto user = userService.getUserDetails(userName, password);
        if (user != null) {
            user.setPassword(null);
            log.info("Successfully retrieved user: {}", userName);
            return ResponseEntity.ok(user);
        } else {
            log.warn("User not found: {}", userName);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<User> saveUser(@RequestBody UserDto user) {
        log.info("Saving new user: {}", user.getUserName());
        User savedUser = userService.saveUser(user);
        log.info("Successfully saved user with ID: {}", savedUser.getUserId());
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        log.info("Updating user with ID: {}", user.getUserId());
        User updatedUser = userService.updateUser(user);
        if (updatedUser != null) {
            log.info("Successfully updated user with ID: {}", user.getUserId());
            return ResponseEntity.ok(updatedUser);
        } else {
            log.warn("User not found for update with ID: {}", user.getUserId());
            return ResponseEntity.notFound().build();
        }

    }
}

