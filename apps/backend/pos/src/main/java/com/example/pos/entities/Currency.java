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
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Represents a currency and its associated configuration.
 */
@Entity
@Table(name = "currency")
@Data
//@NoArgsConstructor
public class Currency {

    /**
     * Currency code (e.g., USD, IDR).
     */
    @Id
    @Column(length = 50)
    private String code;

    /**
     * Human-readable description of the currency.
     */
    @Column(length = 100)
    private String description;

    /**
     * Exchange rate against the base currency.
     */
    @Column(precision = 35, scale = 20, nullable = false)
    private BigDecimal rate;

    /**
     * Whether this currency is the default/base currency.
     */
    @Column(nullable = false)
    private Boolean mainCurrency;

    /**
     * Code of the account used for payables.
     */
    @Column(length = 50)
    private String accountPayable;

    /**
     * Code of the account used for receivables.
     */
    @Column(length = 50)
    private String accountReceivable;

    /**
     * Code of the account used for cash payments.
     */
    @Column(length = 50)
    private String cashPaymentAccount;

    /**
     * Code of the account used for bank payments.
     */
    @Column(length = 50)
    private String bankPaymentAccount;

    /**
     * Currency type (optional field).
     */
    @Column(length = 5)
    private String type;

    /**
     * Constructor to set default values.
     */
    public Currency() {
        this.rate = BigDecimal.ZERO;
        this.mainCurrency = false;
    }
}
