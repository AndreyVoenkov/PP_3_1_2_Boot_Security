package ru.kata.spring.boot_security.demo.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

//Сервис для загрузки пользовательских данных, реализующий интерфейс UserDetailsService.
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository,
                                  RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    //Метод для загрузки пользовательских данных по имени пользователя.
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Ищем пользователя по имени пользователя
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User не найден"));
        Hibernate.initialize(user.getRoles());  // Явная инициализация ролей пользователя
        return user;
    }

    @PostConstruct
    @Transactional
    public void init() {
        // Создание роли ROLE_USER, если он не существует
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            roleRepository.save(new Role("ROLE_USER"));
        }
        // Создание роли ROLE_ADMIN, если он не существует
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        if (adminRole == null) {
            roleRepository.save(new Role("ROLE_ADMIN"));
        }

        // Создание пользователя user, если он не существует
        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user"));
            user.setFirstName("User");
            user.setLastName("User");
            user.setEmail("user@example.com");

            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName("ROLE_USER"));
            user.setRoles(roles);

            userRepository.save(user);
        }

        // Создание пользователя admin, если он не существует
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setEmail("admin@example.com");

            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName("ROLE_USER"));
            roles.add(roleRepository.findByName("ROLE_ADMIN"));
            admin.setRoles(roles);

            userRepository.save(admin);
        }
    }
}
