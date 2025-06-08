/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.entities;

/**
 *
 * @author araneta
 */
public enum AccountCategory {
    CASH("Cash"),
    BANK("Bank"),
    ACCOUNTS_RECEIVABLE("Accounts Receivable"),
    ACCOUNTS_PAYABLE("Accounts Payable"),
    EQUITY("Equity"),
    REVENUE("Revenue"),
    OPERATIONAL_EXPENSE("Operating Expense"),
    NON_OPERATING_EXPENSE("Non-Operating Expense"),
    INVENTORY("Inventory"),
    FIXED_ASSET("Fixed Asset"),
    OTHER("Other");

    private final String label;

    AccountCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

