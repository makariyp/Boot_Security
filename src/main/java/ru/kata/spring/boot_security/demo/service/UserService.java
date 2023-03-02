package ru.kata.spring.boot_security.demo.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Service
public interface UserService {
    void addUser(User user);

    void deleteUser(Long id);

    List<User> getAllUsers();

    void updateUser(User user);

    User findByName(String username);

    User findWithRoles(String name);

    User findById(Long id);
}