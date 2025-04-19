/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.repositories;

import com.example.pos.entities.OfficeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author araneta
 */
@Repository
public interface OfficeConfigRepository extends JpaRepository<OfficeConfig, Long> {
    // You can add custom queries here if needed, e.g.:
    OfficeConfig findByHeadOfficeCode(String headOfficeCode);
}
