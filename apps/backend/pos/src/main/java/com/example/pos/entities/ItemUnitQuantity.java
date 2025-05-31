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
@Table(name = "item_unit_quantity")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemUnitQuantity {

    @Id
    @Column(nullable = false, length = 150)
    private String detailId;

    @Column(length = 100)
    private String itemCode;

    @Column(length = 30)
    private String unit;

    @Column(precision = 20, scale = 3)
    private BigDecimal conversionQuantity;

    @Column(length = 100, unique = true)
    private String barcode;

    @Column(precision = 35, scale = 20)
    private BigDecimal basePrice;

    @Column(length = 20)
    private String type;

    @Column
    private LocalDateTime lastUpdated;

    @Column(precision = 10)
    private BigDecimal points;

    @Column(precision = 20)
    private BigDecimal salesCommission;
}
