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
@Table(name = "item_selling_price")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemSellingPrice {

    @Id
    @Column(nullable = false, length = 150)
    private String detailId;

    @Column(length = 100)
    private String itemCode;

    @Column(length = 10)
    private String priceType;

    @Column(precision = 20, scale = 3)
    private BigDecimal quantityUntil;

    @Column
    private Integer level;

    @Column(precision = 20, scale = 3)
    private BigDecimal percentage;

    @Column(length = 50)
    private String unit;

    @Column(precision = 35, scale = 20)
    private BigDecimal sellingPrice;

    @Column
    private LocalDateTime lastUpdated;
}