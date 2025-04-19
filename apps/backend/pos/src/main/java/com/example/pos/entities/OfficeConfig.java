/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 *
 * @author araneta
 */
@Entity
public class OfficeConfig {
    public OfficeConfig() {}
    @Id    
    private Long id = 1L; // Always ID 1

    
    @Column(name = "head_office_code", nullable = false)
    private String headOfficeCode;
    
    @Column(name = "office_name", nullable = false)
    private String officeName;
    
    @Column(name = "timezone", nullable = false)
    private String timezone;

    @Column(name = "start_month", nullable = false)
    private Integer startMonth; // e.g., "April"

    @Column(name = "start_year", nullable = false)
    private Integer startYear;

    @Column(name = "end_month", nullable = false)
    private Integer endMonth; // e.g., "Desember"

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the headOfficeCode
     */
    public String getHeadOfficeCode() {
        return headOfficeCode;
    }

    /**
     * @param headOfficeCode the headOfficeCode to set
     */
    public void setHeadOfficeCode(String headOfficeCode) {
        this.headOfficeCode = headOfficeCode;
    }

    /**
     * @return the officeName
     */
    public String getOfficeName() {
        return officeName;
    }

    /**
     * @param officeName the officeName to set
     */
    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    /**
     * @return the timezone
     */
    public String getTimezone() {
        return timezone;
    }

    /**
     * @param timezone the timezone to set
     */
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    /**
     * @return the startMonth
     */
    public Integer getStartMonth() {
        return startMonth;
    }

    /**
     * @param startMonth the startMonth to set
     */
    public void setStartMonth(Integer startMonth) {
        this.startMonth = startMonth;
    }

    /**
     * @return the startYear
     */
    public Integer getStartYear() {
        return startYear;
    }

    /**
     * @param startYear the startYear to set
     */
    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    /**
     * @return the endMonth
     */
    public Integer getEndMonth() {
        return endMonth;
    }

    /**
     * @param endMonth the endMonth to set
     */
    public void setEndMonth(Integer endMonth) {
        this.endMonth = endMonth;
    }
}
