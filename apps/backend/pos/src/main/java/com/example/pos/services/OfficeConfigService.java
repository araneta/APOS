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

    public OfficeConfig save(OfficeConfig config) {
        return officeConfigRepository.save(config);
    }

    public List<OfficeConfig> findAll() {
        return officeConfigRepository.findAll();
    }

    public Optional<OfficeConfig> findById(Long id) {
        return officeConfigRepository.findById(id);
    }

    public Optional<OfficeConfig> findByHeadOfficeCode(String code) {
        return Optional.ofNullable(officeConfigRepository.findByHeadOfficeCode(code));
    }

    public void deleteById(Long id) {
        officeConfigRepository.deleteById(id);
    }
}
