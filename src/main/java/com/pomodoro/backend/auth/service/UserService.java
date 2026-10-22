package com.pomodoro.backend.auth.service;

import com.pomodoro.backend.auth.model.Role;
import com.pomodoro.backend.auth.model.User;
import com.pomodoro.backend.auth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<String> getUserRolesByEmail(String email) {
        return userRepository.findRolesOfUserByEmail(email);
    }

    public List<String> getUserRolesById(UUID uuid) {
        return userRepository.findRolesOfUserById(uuid);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}
