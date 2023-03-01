package ru.kata.spring.boot_security.demo.controller.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;

@RequestMapping("/api/admin")
@RestController
public class AdminRestController {
    private final UserService userService;
    private final RoleDao roleDao;

    @Autowired
    public AdminRestController(UserService userService, RoleDao roleDao) {
        this.userService = userService;
        this.roleDao = roleDao;
    }

    @GetMapping(value = "users")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.FOUND);
    }

    @GetMapping(value = "user")
    public ResponseEntity<User> getCurrentUsers() {
        return new ResponseEntity<>((User) SecurityContextHolder
                                                    .getContext()
                                                    .getAuthentication()
                                                    .getPrincipal(),
                                    HttpStatus.FOUND);
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
        return new ResponseEntity<>(userService.findByName(user.getName()), HttpStatus.CREATED);
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