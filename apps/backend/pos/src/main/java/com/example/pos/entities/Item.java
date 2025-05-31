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
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    @Column(nullable = false, length = 100, unique = true)
    private String itemCode;

    @Column(columnDefinition = "text")
    private String itemName;

    @Column(length = 50)
    private String category;

    @Column(length = 15)
    private String type;

    @Column(length = 50)
    private String currency;

    @Column(length = 15)
    private String serial;

    @Column(length = 15)
    private String consignment;

    @Column(precision = 20, scale = 3)
    private BigDecimal minimumStock;

    @Column(length = 1)
    private String priceSystem;

    @Column
    private Boolean enablePriceOptions;

    @Column(length = 100)
    private String rack;

    @Column(length = 50)
    private String unit;

    @Column(precision = 35, scale = 20)
    private BigDecimal basePrice;

    @Column(precision = 20, scale = 3)
    private BigDecimal pricePercentage1;

    @Column(precision = 20, scale = 3)
    private BigDecimal sellingPrice1;

    @Column(columnDefinition = "text")
    private String description;

    @Column(length = 50)
    private String supplier1;

    @Column(length = 50)
    private String supplier2;

    @Column(length = 50)
    private String supplier3;

    @Column
    private byte[] image;

    @Column(length = 15)
    private String salesStatus;

    @Column(length = 50)
    private String brand;

    @Column(length = 10)
    private String hppSystem;

    @Column
    private Integer taxSystem;

    @Column
    private Boolean enableFlexiblePrice;

    @Column(precision = 20, scale = 3)
    private BigDecimal assemblyPrice;

    @Column(length = 15)
    private String deleteStatus;

    @Column(precision = 20, scale = 3)
    private BigDecimal stock;

    @Column(length = 50)
    private String department;

    @Column(length = 15)
    private String refrigerated;

    @Column(length = 30)
    private String hppAccount;

    @Column(length = 30)
    private String revenueAccount;

    @Column(length = 30)
    private String inventoryAccount;

    @Column(length = 30)
    private String serviceAccount;

    @Column(length = 30)
    private String nonInventoryAccount;

    @Column(length = 30)
    private String rawMaterialAccount;

    @Column(length = 30)
    private String laborCostAccount;

    @Column(length = 30)
    private String overheadCostAccount;

    @Column
    private LocalDateTime lastUpdated;

    @Column(precision = 20, scale = 3)
    private BigDecimal tempHpp;

    @Column(precision = 20, scale = 3)
    private BigDecimal tempQuantity;

    @Column(precision = 20, scale = 3)
    private BigDecimal tempValue;

    @Column(columnDefinition = "text")
    private String imageFile;

    @Column
    private LocalDateTime dateAdded;

    @Column
    private Boolean enableAssemblyPrice;

    @Column
    private Boolean isTaxExempt;

    @Column
    private Boolean defaultBasePriceOption;
}
