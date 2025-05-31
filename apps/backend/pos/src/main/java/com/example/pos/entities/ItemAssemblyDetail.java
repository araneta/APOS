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
@Table(name = "item_assembly_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemAssemblyDetail {

    @Id
    @Column(length = 100)
    private String id;

    @Column(length = 100)
    private String itemCode;

    @Column(length = 100)
    private String assemblyItemCode;

    @Column(precision = 20, scale = 3)
    private BigDecimal quantity;

    @Column(length = 50)
    private String unit;

    @Column(precision = 20, scale = 3)
    private BigDecimal price;

    @Column(precision = 20, scale = 3)
    private BigDecimal total;

    private LocalDateTime updatedAt;

    @Column(length = 20)
    private String type;
}