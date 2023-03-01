package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;
    private UserService userService;
    private final RoleDao roleDao;

    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserService userService, RoleDao roleDao) {
        this.successUserHandler = successUserHandler;
        this.userService = userService;
        this.roleDao = roleDao;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/index").permitAll()
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .and()
                .formLogin().successHandler(successUserHandler)
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return name -> {
            User user = userService.findByName(name);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return user;
        };
    }

    @PostConstruct
    public void defaultAdminAndRoles() {
        if (roleDao.findByName("ROLE_USER") == null) {
            roleDao.save(new Role("ROLE_USER"));
        }
        if (roleDao.findByName("ROLE_ADMIN") == null) {
            roleDao.save(new Role("ROLE_ADMIN"));
        }
        if (userService.findByName("admin") == null) {
            User admin = new User();
            admin.setName("admin");
            admin.setPassword("admin");
            admin.setRoles(Set.of(roleDao.findByName("ROLE_ADMIN"), roleDao.findByName("ROLE_USER")));
            userService.addUser(admin);
        }
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
}