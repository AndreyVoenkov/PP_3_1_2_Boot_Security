package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.service.UserDetailsServiceImpl;


/**
 * Класс конфигурации безопасности веб-приложения.
 * Аннотация @EnableWebSecurity включает поддержку безопасности веб-приложения.
 * Аннотация @EnableGlobalMethodSecurity позволяет использовать аннотации безопасности
 * на уровне методов, таких как @PreAuthorize и @PostAuthorize.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SuccessUserHandler successUserHandler;
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }

    @Autowired
    public void setUserDetailsServiceImpl(@Lazy UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    /**
     * Определение бина для кодировщика паролей.
     * BCryptPasswordEncoder используется для хеширования паролей.
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Метод configure(HttpSecurity http): Настраивает правила доступа к URL,
     * страницы входа и выхода, обработчики успешной и неудачной аутентификации.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // Разрешаем доступ к указанным URL для всех пользователей
                .antMatchers("/", "/index", "/login", "/registration", "/error").permitAll()
                // Доступ к URL с префиксом /admin только для пользователей с ролью ADMIN
                .antMatchers("/admin/**").hasRole("ADMIN")
                // Все остальные запросы требуют аутентификации
                .anyRequest().authenticated()
                .and()
                // Настройка формы входа
                .formLogin().successHandler(successUserHandler)
                .loginPage("/login")
                .loginProcessingUrl("/process_login")
                .failureUrl("/login?error")
                .and()
                // Настройка выхода из системы
                .logout().logoutUrl("/logout")
                .logoutSuccessUrl("/").permitAll()
                .and()
                // Отключение CSRF защиты для упрощения конфигурации
                .csrf().disable();
    }

    /**
     * Метод configure(AuthenticationManagerBuilder auth) настраивает аутентификацию,
     * используя UserDetailsServiceImpl и PasswordEncoder.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl)
                .passwordEncoder(passwordEncoder());
    }
}