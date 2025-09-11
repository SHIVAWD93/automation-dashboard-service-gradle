package com.qa.automation.controller;

import com.qa.automation.dto.UserDto;
import com.qa.automation.model.User;
import com.qa.automation.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<UserDto> getUser(@RequestParam String userName, @RequestParam String password) {
        try {
            log.info("Fetching user details for username: {}", userName);
            UserDto user = userService.getUserDetails(userName, password);
            if (user != null) {
                user.setPassword(null);
                log.info("Successfully retrieved user: {}", userName);
                return ResponseEntity.ok(user);
            }
            else {
                log.warn("User not found: {}", userName);
                return ResponseEntity.notFound().build();
            }
        }
        catch (Exception e) {
            log.error("Error fetching user {}: {}", userName, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<User> saveUser(@RequestBody UserDto user) {
        try {
            log.info("Saving new user: {}", user.getUserName());
            User savedUser = userService.saveUser(user);
            log.info("Successfully saved user with ID: {}", savedUser.getUserId());
            return ResponseEntity.ok(savedUser);
        }
        catch (Exception e) {
            log.error("Error saving user {}: {}", user.getUserName(), e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        try {
            log.info("Updating user with ID: {}", user.getUserId());
            User updatedUser = userService.updateUser(user);
            if (updatedUser != null) {
                log.info("Successfully updated user with ID: {}", user.getUserId());
                return ResponseEntity.ok(updatedUser);
            }
            else {
                log.warn("User not found for update with ID: {}", user.getUserId());
                return ResponseEntity.notFound().build();
            }
        }
        catch (Exception e) {
            log.error("Error updating user with ID {}: {}", user.getUserId(), e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}

