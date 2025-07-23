/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.dto;
import com.example.pos.entities.AccountCategory;
import com.example.pos.entities.AccountType;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
/**
 *
 * @author araneta
 */
public class AccountEntryForm {

    /**
     * @return the accountCategory
     */
    public String getAccountCategory() {
        return accountCategory;
    }

    /**
     * @param accountCategory the accountCategory to set
     */
    public void setAccountCategory(String accountCategory) {
        this.accountCategory = accountCategory;
    }

    /**
     * @return the accountType
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * @param accountType the accountType to set
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the codePrefix
     */
    public String getCodePrefix() {
        return codePrefix;
    }

    /**
     * @param codePrefix the codePrefix to set
     */
    public void setCodePrefix(String codePrefix) {
        this.codePrefix = codePrefix;
    }

    /**
     * @return the isCashBank
     */
    public boolean isIsCashBank() {
        return isCashBank;
    }

    /**
     * @param isCashBank the isCashBank to set
     */
    public void setIsCashBank(boolean isCashBank) {
        this.isCashBank = isCashBank;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the parentAccount
     */
    public String getParentAccount() {
        return parentAccount;
    }

    /**
     * @param parentAccount the parentAccount to set
     */
    public void setParentAccount(String parentAccount) {
        this.parentAccount = parentAccount;
    }
    private String accountCategory;
    
    private String accountType;
    
    @NotNull(message="cannot be null")
    @Size(min=1, message="cannot be blank")
    private String code;
    
    @NotNull(message="cannot be null")
    @Size(min=1, message="cannot be blank")
    private String codePrefix;
    
    private boolean isCashBank;
    
    @NotNull(message="cannot be null")
    @Size(min=1, message="cannot be blank")
    private String name;
    
    @NotNull(message="cannot be null")
    @Size(min=1, message="cannot be blank")
    private String parentAccount;
}
