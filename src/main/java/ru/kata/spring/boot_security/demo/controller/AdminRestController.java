package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {
    private final UserService userService;

    @Autowired
    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "users")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.FOUND);
    }

    @GetMapping(value = "user/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        userService.addUser(user);
        return new ResponseEntity<>(userService.findById(user.getId()), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity<User> deleteUser(@RequestBody User user) {
        userService.deleteUser(user.getId());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping(value = "edit")
    public ResponseEntity<User> editSubmit(@RequestBody User user) {
        userService.updateUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}