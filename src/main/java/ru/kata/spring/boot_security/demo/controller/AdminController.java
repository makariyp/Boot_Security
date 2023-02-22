package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Set;

@Controller
public class AdminController {
    private final UserService userService;
    private RoleDao roleDao;

    @Autowired
    public AdminController(UserService userService, RoleDao roleDao) {
        this.userService = userService;
        this.roleDao = roleDao;
    }


    @GetMapping(value = "/admin")
    public String getIndex(ModelMap model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        Set<Role> roles = Set.copyOf(roleDao.findAll());
        model.addAttribute("allRoles", roles);
        return "admin/index";
    }

    @PostMapping(value = "/admin/add")
    public String addUser(@ModelAttribute User user) {
        userService.addUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping(value = "/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping(value = "/admin/edit/{id}")
    public String editPage(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("user", userService.getById(id));
        Set<Role> roles = Set.copyOf(roleDao.findAll());
        model.addAttribute("allRoles", roles);
        return "admin/edit";
    }

    @PatchMapping(value = "/admin/edit")
    public String editSubmit(@ModelAttribute User user) {
        userService.updateUser(user);
        return "redirect:/admin";
    }
}