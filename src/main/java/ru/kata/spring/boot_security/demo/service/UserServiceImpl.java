package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepositoryCustom;


import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepositoryCustom userRepositoryCustom;

    @Autowired
    public UserServiceImpl(UserRepositoryCustom userRepositoryCustom) {
        this.userRepositoryCustom = userRepositoryCustom;
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public void add(User user) {
        userRepositoryCustom.add(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public List<User> getAllUsers() {
        return userRepositoryCustom.getAllUsers();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public User getUser(Long id) {
        return userRepositoryCustom.getUser(id);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public void deleteUser(Long id) {
        userRepositoryCustom.deleteUser(id);
    }
}
