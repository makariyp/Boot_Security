package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.model.User;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    @GetMapping(value = "user")
    public ResponseEntity<User> getCurrentUsers() {
        return new ResponseEntity<>((User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal(),
                HttpStatus.FOUND);
    }
}
