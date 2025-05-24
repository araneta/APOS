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

/**
 * Represents a brand for items.
 */
@Entity
@Table(name = "item_brand")
@Data
public class ItemBrand {

    @Id
    @Column(length = 50)
    private String code;

    @Column(length = 100)
    private String description;
}