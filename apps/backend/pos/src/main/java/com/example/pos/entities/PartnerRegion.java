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

@Entity
@Table(name = "partner_region")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartnerRegion {

    @Id
    @Column(length = 50)
    private String code;

    @Column(length = 250)
    private String name;
}