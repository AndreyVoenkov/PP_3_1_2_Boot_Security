package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Optional;

//Сервис для загрузки пользовательских данных, реализующий интерфейс UserDetailsService.
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Метод для загрузки пользовательских данных по имени пользователя.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Ищем пользователя по имени пользователя
        Optional<User> user = userRepository.findByUsername(username);
        // Если пользователь не найден, выбрасываем исключение
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User не найден");
        }
        // Возвращаем найденного пользователя
        return user.get();
    }
}
