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
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import com.example.pos.entities.Currency;


@Entity
public class Account {

    @Id
    @Column(length = 30)
    private String code;

    @ManyToOne
    @JoinColumn(name = "parent_code")
    private Account parent;

    @Column(length = 2)
    private String group;

    @Column(length = 2)
    private String type;

    @Column(length = 200)
    private String name;

    @ManyToOne
    @JoinColumn(name = "currency_code")
    private Currency currency;

    private LocalDateTime lastUpdated;

    private Boolean cashOrBank = false;

    private Boolean defaultMainCurrency = false;

    // Getters and Setters

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the parent
     */
    public Account getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Account parent) {
        this.parent = parent;
    }

    /**
     * @return the group
     */
    public String getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the currency
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     * @return the lastUpdated
     */
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @param lastUpdated the lastUpdated to set
     */
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * @return the cashOrBank
     */
    public Boolean getCashOrBank() {
        return cashOrBank;
    }

    /**
     * @param cashOrBank the cashOrBank to set
     */
    public void setCashOrBank(Boolean cashOrBank) {
        this.cashOrBank = cashOrBank;
    }

    /**
     * @return the defaultMainCurrency
     */
    public Boolean getDefaultMainCurrency() {
        return defaultMainCurrency;
    }

    /**
     * @param defaultMainCurrency the defaultMainCurrency to set
     */
    public void setDefaultMainCurrency(Boolean defaultMainCurrency) {
        this.defaultMainCurrency = defaultMainCurrency;
    }
}
