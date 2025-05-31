/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "partner")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Partner {

    @EmbeddedId
    private PartnerId id;

    @Column(length = 150)
    private String name;

    @Column(columnDefinition = "address")
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String province;

    @Column(length = 20)
    private String postalCode;

    @Column(length = 100)
    private String country;

    @Column(length = 200)
    private String phone;

    @Column(length = 200)
    private String fax;

    @Column(length = 200)
    private String contact;

    @Column(length = 200)
    private String email;

    @ManyToOne
    @JoinColumn(name = "currency")
    private Currency currency;

    @Column(length = 100)
    private String accountNumber;

    @Column(length = 100)
    private String accountHolder;

    @Column(length = 100)
    private String bank;

    @Column(columnDefinition = "text")
    private String notes;

    private BigDecimal creditLimitAmount;

    private Integer creditLimitDays;

    @Column(length = 5)
    private String discountType;

    @Column(name = "group_code", length = 20)
    private String groupCode;

    private Integer commissionMode;

    private Integer commissionCalcMethod;

    private BigDecimal commissionPercent;

    private BigDecimal commissionAmount;

    @Column(length = 100)
    private String taxIdNumber;

    private Integer invoiceDueDays;

    @ManyToOne
    @JoinColumn(name = "partner_region")
    private PartnerRegion region;

    @ManyToOne
    @JoinColumn(name = "partner_subregion")
    private PartnerSubregion subregion;

    @Column(length = 50)
    private String salesCode;

    private BigDecimal maxCreditAmount;

    @Column(length = 10)
    private String taxSystem;

    @Column(length = 10)
    private String optionalTaxSystem;

    private BigDecimal taxAmount;

    @Column(length = 50)
    private String nationalId;

    @Column(length = 150)
    private String taxName;

    @Column(columnDefinition = "text")
    private String taxAddress;

    private LocalDateTime birthDate;

    @Column(length = 5)
    private String creditOption;

    @Column(length = 30)
    private String creditAccount;

    @Column(length = 15)
    private String status = "Y";

    private boolean checkBeforePaid = false;
}