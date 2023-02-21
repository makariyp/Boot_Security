package ru.kata.spring.boot_security.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
@Repository
public interface UserDao extends JpaRepository<User, Long> {
    User getById(Long id);
    @Query("from User user join fetch user.roles c where user.name = ?1")
    User findByName(String username);
}