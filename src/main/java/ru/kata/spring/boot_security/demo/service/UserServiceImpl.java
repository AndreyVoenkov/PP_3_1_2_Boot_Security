package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;


import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public void add(User user) {
        userDao.add(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public User getUser(Long id) {
        return userDao.getUser(id);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }
}
