/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.services;

/**
 *
 * @author aldo
 */
import com.example.pos.dto.SecurityUser;
import com.example.pos.entities.User;
import com.example.pos.repositories.UserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username);

        // If user not found, throw exception
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Return UserDetails with username, password, and roles
        /*
        return org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRole()) // You can split and add multiple roles if needed
                .build();
         */
        List<GrantedAuthority> authorities = Arrays.stream(userEntity.getRole().split(","))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim()))
                .collect(Collectors.toList());
        // Return UserDetails with username, password, and authorities
        return new SecurityUser(
                userEntity.getUsername(),
                userEntity.getPassword(),
                true, // Account is enabled
                true, // Account is not expired
                true, // Credentials are not expired
                true, // Account is not locked
                authorities,
                userEntity.getId() // Add custom field if needed
        );
    }
}
