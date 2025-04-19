/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.services;

import com.example.pos.dto.UpdateProfileRequest;
import com.example.pos.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author araneta
 */
@Service
public class ProfileService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public void updateProfile(UpdateProfileRequest profile){
        var user = userRepository.findByUsername(profile.getUsername());
        //if (user != null) {
          //  throw new RuntimeException("Username already exists!");
        //}
        user.setFirstName(profile.getFirstName());
        user.setLastName(profile.getLastName());
        if(!user.getPassword().isBlank()){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        userRepository.save(user);
    }

    public UpdateProfileRequest getProfile(String username) {
        var user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found!"+username);
        }
        var profile = new UpdateProfileRequest();
        profile.setUsername(user.getUsername());
        profile.setFirstName(user.getFirstName());
        profile.setLastName(user.getLastName());
        return profile;
    }
}
