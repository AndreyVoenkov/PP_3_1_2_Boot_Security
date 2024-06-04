package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserDetailsServiceImpl;


@Component
public class UserValidator implements Validator {

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public UserValidator(UserDetailsServiceImpl userServiceDetailsService) {
        this.userDetailsServiceImpl = userServiceDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        try {
            userDetailsServiceImpl.loadUserByUsername(user.getUsername());
        } catch (UsernameNotFoundException ignor) {
            return;
        }
        errors.rejectValue("username", "", "User с таким именем существует");
    }
}
