/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.entities;

/**
 *
 * @author araneta
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String code;

    @Column(length = 100, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Account parent;

    @Column(nullable = false)
    private Integer level = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;

    @Enumerated(EnumType.STRING)
    @Column
    private AccountCategory category;

    @Column(name = "is_cash_bank", nullable = false)
    private boolean isCashBank = false;

    @Column(name = "is_fx_default", nullable = false)
    private boolean isFxDefault = false;

    @Column(length = 10)
    private String currency;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    
    

    public Account(String code, String name, AccountType type, AccountCategory category, Account parent, BigDecimal balance) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.category = category;
        this.parent = parent;
        this.level = (parent != null) ? parent.getLevel() + 1 : 0;
        this.isActive = true;
        this.isCashBank = false;
        this.isFxDefault = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setParent(Account parent) {
        this.parent = parent;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public void setCategory(AccountCategory category) {
        this.category = category;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setCashBank(boolean isCashBank) {
        this.isCashBank = isCashBank;
    }

    public void setFxDefault(boolean isFxDefault) {
        this.isFxDefault = isFxDefault;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}