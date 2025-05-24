/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.entities;

/**
 *
 * @author araneta
 */

import java.io.Serializable;
import java.util.Objects;

public class PartnerId implements Serializable {

    private String code;
    private String type;

    public PartnerId() {}

    public PartnerId(String code, String type) {
        this.code = code;
        this.type = type;
    }

    // equals and hashCode required for @IdClass
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PartnerId)) return false;
        PartnerId that = (PartnerId) o;
        return Objects.equals(code, that.code) &&
               Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, type);
    }
}