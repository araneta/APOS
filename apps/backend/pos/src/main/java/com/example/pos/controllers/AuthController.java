/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.controllers;

/**
 *
 * @author aldo
 */
import com.example.pos.dto.BaseResponse;
import com.example.pos.dto.JwtResponse;
import com.example.pos.dto.LoginRequest;
import com.example.pos.dto.SecurityUser;
import com.example.pos.entities.User;
import com.example.pos.services.AuthService;
import com.example.pos.config.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        var createdUser = authService.signup(user);
        var response = new BaseResponse();
        response.setMessage("Success");
        response.setData(createdUser);
        response.setStatus("ok");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            logger.info(loginRequest.getUsername());
            logger.info(loginRequest.getPassword());
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(                    
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            // Generate JWT token
            SecurityUser userDetails = (SecurityUser)authentication.getPrincipal();
            //userDetails.

            String token = jwtTokenUtil.generateToken(userDetails.getUserID(), userDetails.getUsername());

            // Return token
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (Exception e) {
            e.printStackTrace();
            //return ResponseEntity.status(401).body("Invalid username or password");
            var response = new BaseResponse();
            response.setMessage("Invalid username or password1");
            //response.setData(createdUser);
            response.setStatus("error");
            return ResponseEntity.status(401).body(response);
        }
    }

     

    
}
