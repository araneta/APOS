/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.controllers;

import com.example.pos.dto.AccountDTO;
import com.example.pos.entities.Account;
import com.example.pos.services.AccountService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService cashOfAccountService;

    @Autowired
    public AccountController(AccountService cashOfAccountService) {
        this.cashOfAccountService = cashOfAccountService;
    }

    @GetMapping
    public List<AccountDTO> getAllAccounts() {
        return cashOfAccountService.getAllAccounts().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AccountDTO convertToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setCode(account.getCode());
        dto.setName(account.getName());
        dto.setLevel(account.getLevel());
        dto.setType(account.getType());
        dto.setCategory(account.getCategory());
        dto.setCashBank(account.isCashBank());
        dto.setFxDefault(account.isFxDefault());
        dto.setCurrency(account.getCurrency());
        dto.setActive(account.isActive());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setUpdatedAt(account.getUpdatedAt());
        
        // Handle parent account if it exists
        if (account.getParent() != null) {
            AccountDTO parentDTO = new AccountDTO();
            parentDTO.setId(account.getParent().getId());
            parentDTO.setCode(account.getParent().getCode());
            parentDTO.setName(account.getParent().getName());
            dto.setParent(parentDTO);
        }
        
        return dto;
    }
}
