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
import java.time.LocalDate;

@Entity
@Table(name = "partner")
@Data
@IdClass(PartnerId.class)
public class Partner {

    @Id
    private String code;

    @Id
    private String type;

    private String name;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private String country;
    private String phone;
    private String fax;
    private String contactPerson;
    private String email;

    private String currencyCode;

    private String bankAccountNumber;
    private String accountHolderName;
    private String bankName;
    private String note;

    private BigDecimal invoiceAmountLimit;
    private Integer invoiceDueDaysLimit;
    private String discountType;
    private String groupCode;
    private Integer commissionOption;
    private Integer commissionAmountOption;
    private BigDecimal commissionPercent;
    private BigDecimal commissionNominal;

    private String taxIdNumber;
    private Integer paymentDueDay;

    private String regionCode;
    private String subRegionCode;
    private String salesCode;

    private BigDecimal creditLimit;
    private String taxSystem;
    private String optionalTaxSystem;
    private BigDecimal taxAmount;

    private String nationalIdNumber;
    private String taxpayerName;
    private String taxpayerAddress;
    private LocalDate birthDate;
    private String creditOption;
    private String creditAccountCode;
    private String isActive;

    private boolean checkOutstandingBalance;
}