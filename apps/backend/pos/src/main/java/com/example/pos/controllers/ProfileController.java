/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.controllers;

import com.example.pos.dto.BaseResponse;
import com.example.pos.dto.UpdateProfileRequest;
import com.example.pos.services.ProfileService;
import com.example.pos.config.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * @author araneta
 */
@RestController
@RequestMapping("/api/users")
public class ProfileController {
    
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @PutMapping("/profile")
    public ResponseEntity<?> saveProfile(@RequestBody UpdateProfileRequest form){
        profileService.updateProfile(form);
        var response = new BaseResponse();
        response.setMessage("Success");
        response.setData(form);
        response.setStatus("ok");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtTokenUtil.extractUsername(token);
            var profile = profileService.getProfile(username);
            var response = new BaseResponse();
            response.setMessage("Success");
            response.setData(profile);
            response.setStatus("ok");
            return ResponseEntity.ok(response);
        }
        var response = new BaseResponse();
        response.setMessage("Unauthorized");
        response.setStatus("error");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
