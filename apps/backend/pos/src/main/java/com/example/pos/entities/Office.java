/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.entities;

/**
 *
 * @author araneta
 */
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Represents an office or branch in the POS system.
 */
@Entity
@Table(name = "office")
@Data
public class Office {

    @Id
    @Column(length = 50)
    private String code;

    @Column(length = 20)
    private String function;

    @Column(length = 200)
    private String name;

    @Column(columnDefinition = "text")
    private String address;

    @Column(length = 150)
    private String phoneNumber;

    @Column(length = 150)
    private String fax;

    @Column(nullable = false)
    private Boolean branch = false;

    /**
     * Associated account (many offices can share the same account).
     */
    @ManyToOne
    @JoinColumn(name = "account_code")
    private Account account;

    @Column(nullable = false)
    private Boolean mobile = false;

    @Column(nullable = false)
    private Boolean enabled = false;

    @Column(precision = 20, scale = 3, nullable = false)
    private BigDecimal taxNumber = BigDecimal.ZERO;

    @Column(length = 15)
    private String activeStatus = "Y";

    @Column(length = 20)
    private String whatsappNumber;

    @Column(length = 100)
    private String email;
}