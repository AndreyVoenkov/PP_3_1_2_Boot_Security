package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;


@Configuration
public class DatabaseInitializer {

    @Autowired
    private RoleRepository roleRepository;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            addRoleIfNotExists("ROLE_USER");
            addRoleIfNotExists("ROLE_ADMIN");
        };
    }

    private void addRoleIfNotExists(String roleName) {
        if (!roleRepository.existsByName(roleName)) {
            roleRepository.save(new Role(roleName));
        }
    }
}
