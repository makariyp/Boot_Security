package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    @Override
    public void addUser(User user) {
        userDao.save(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userDao.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        userDao.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User getById(Long id) {
        return userDao.getById(id);
    }

    @Override
    public User findByName(String username) {
        return userDao.findByName(username);
    }

    @Transactional
    @Override
    public User save(User user) {
        return userDao.save(user);
    }
}