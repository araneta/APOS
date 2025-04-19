/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.controllers;

import com.example.pos.entities.OfficeConfig;
import com.example.pos.services.OfficeConfigService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author araneta
 */
@RestController
@RequestMapping("/api/config")
public class OfficeConfigController {

    private final OfficeConfigService officeConfigService;

    @Autowired
    public OfficeConfigController(OfficeConfigService officeConfigService) {
        this.officeConfigService = officeConfigService;
    }

    @GetMapping
    public List<OfficeConfig> getAllConfigs() {
        return officeConfigService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfficeConfig> getConfigById(@PathVariable Long id) {
        return officeConfigService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/kode/{code}")
    public ResponseEntity<OfficeConfig> getByHeadOfficeCode(@PathVariable String code) {
        return officeConfigService.findByHeadOfficeCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public OfficeConfig createConfig(@RequestBody OfficeConfig config) {
        return officeConfigService.save(config);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfficeConfig> updateConfig(@PathVariable Long id, @RequestBody OfficeConfig updatedConfig) {
        return officeConfigService.findById(id).map(existing -> {
            existing.setHeadOfficeCode(updatedConfig.getHeadOfficeCode());
            existing.setOfficeName(updatedConfig.getOfficeName());
            existing.setTimezone(updatedConfig.getTimezone());
            existing.setStartMonth(updatedConfig.getStartMonth());
            existing.setStartYear(updatedConfig.getStartYear());
            existing.setEndMonth(updatedConfig.getEndMonth());
            return ResponseEntity.ok(officeConfigService.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConfig(@PathVariable Long id) {
        if (officeConfigService.findById(id).isPresent()) {
            officeConfigService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}