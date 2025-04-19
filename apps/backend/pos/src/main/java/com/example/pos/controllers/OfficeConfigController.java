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
    public OfficeConfig getConfig() {
        return officeConfigService.get();
    }

    @PostMapping
    public OfficeConfig saveConfig(@RequestBody OfficeConfig config) {
        return officeConfigService.saveOrUpdate(config);
    }
}