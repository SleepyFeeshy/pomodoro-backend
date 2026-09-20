package com.pomodoro.backend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pomodoro.backend.user.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
