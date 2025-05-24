/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.entities;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
/**
 *
 * @author araneta
 */
@Entity
@Table(name = "item_unit")
@Data
public class ItemUnit {

    @Id
    @Column(length = 50)
    private String code;

    @Column(length = 100)
    private String description;

    @Column(precision = 20, scale = 3)
    private BigDecimal conversionValue;

    @Column(length = 50)
    private String conversionUnitCode;

    private boolean isPrimary;
}