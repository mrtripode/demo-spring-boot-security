package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.example.demo.security.user.Permission.*;
import static com.example.demo.security.user.Role.*;
import static com.example.util.ApiPath.API_MANAGEMENT;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/", "index.html", "/css/*", "/js/*").permitAll()
            // Only allow access students with role STUDENT
            .antMatchers("/api/**").hasRole(STUDENT.name())
            // Allow access students with role STUDENT, ADMIN and ADMINTRAINEE
            .antMatchers("/api/**").hasAuthority(STUDENT_READ.getPermission())
            .antMatchers(GET, API_MANAGEMENT).hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())
            .antMatchers(DELETE, API_MANAGEMENT).hasAuthority(COURSE_WRITE.getPermission())
            .antMatchers(POST, API_MANAGEMENT).hasAuthority(COURSE_WRITE.getPermission())
            .antMatchers(PUT, API_MANAGEMENT).hasAuthority(COURSE_WRITE.getPermission())
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic();
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails annaSmith = User.builder()
                .username("annasmith")
                .password(passwordEncoder.encode("password"))
//                .roles(STUDENT.name()) // ROLE_STUDENT
                .authorities(STUDENT.getGrantedAuthorities())
                .build();
        UserDetails linda = User.builder()
                .username("linda")
                .password(passwordEncoder.encode("password123"))
 //               .roles(ADMIN.name()) // ROLE_ADMIN
                .authorities(ADMIN.getGrantedAuthorities())
                .build();
        UserDetails tom = User.builder()
                .username("tom")
                .password(passwordEncoder.encode("password123"))
//                .roles(ADMINTRAINEE.name()) // ROLE_ADMINTRAINEE
                .authorities(ADMINTRAINEE.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(
            annaSmith,
            linda,
            tom
        );
    }

}
