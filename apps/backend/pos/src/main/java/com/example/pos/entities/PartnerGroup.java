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

@Entity
@Table(name = "partner_group")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartnerGroup {

    @Id
    @Column(length = 20)
    private String code;

    @Column(length = 100)
    private String name;

    @Column(precision = 20, scale = 3)
    private BigDecimal discount;

    private Integer priceLevel;

    @Column(precision = 20, scale = 3)
    private BigDecimal pointMultiplier;
}
