/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.services;

import com.example.pos.entities.OfficeConfig;
import com.example.pos.repositories.OfficeConfigRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author araneta
 */
@Service
public class OfficeConfigService {
    private final OfficeConfigRepository officeConfigRepository;

    @Autowired
    public OfficeConfigService(OfficeConfigRepository officeConfigRepository) {
        this.officeConfigRepository = officeConfigRepository;
    }

   public OfficeConfig get() {
        return officeConfigRepository.findById(1L).orElse(null);
    }

    public OfficeConfig saveOrUpdate(OfficeConfig config) {
        config.setId(1L); // Force ID 1
        return officeConfigRepository.save(config);
    }
}
