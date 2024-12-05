/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.repositories;

import com.example.pos.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author aldo
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    
}
