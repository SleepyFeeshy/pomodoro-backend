package com.pomodoro.backend.user;

import org.springframework.web.bind.annotation.*;

import com.pomodoro.backend.user.User;
import com.pomodoro.backend.user.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    // CREATE (POST)
    @PostMapping
    public User addUser(@RequestBody User user) {
        return repository.save(user);
    }

    // READ (GET ALL)
    @GetMapping
    public List<User> getAllUser() {
        return repository.findAll();
    }

//    @GetMapping("/user")
//    public String hello() {
//        return "Hello";
//    }
}
