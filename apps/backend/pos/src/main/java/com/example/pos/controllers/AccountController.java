/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.controllers;

import com.example.pos.dto.AccountDTO;
import com.example.pos.dto.Paging;
import com.example.pos.dto.PagingResult;
import com.example.pos.entities.Account;
import com.example.pos.services.AccountService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<AccountDTO> getAllAccounts() {
        return accountService.getAllAccounts().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @GetMapping("/search")    
    public PagingResult<AccountDTO> searchAccounts(@ModelAttribute Paging paging) {
        paging.setValidCols(List.of("name", "code"));
        paging.validateSort();
        paging.init(); // Ensure calculation is done
        
        PagingResult<Account> result = accountService.searchAccounts(paging);
        PagingResult<AccountDTO> dtoResult = new PagingResult<>();
        
        // Convert the data to DTOs
        List<AccountDTO> dtoList = result.getData().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        // Copy pagination metadata
        dtoResult.setData(dtoList);
        dtoResult.setTotalRecords(result.getTotalRecords());
        dtoResult.setTotalDisplayRecords(result.getTotalDisplayRecords());
        dtoResult.setPage(result.getPage());
        dtoResult.setTotalPages(result.getTotalPages());
        dtoResult.setStart(result.getStart());
        dtoResult.setEnd(result.getEnd());
        dtoResult.setSort(result.getSort());
        
        return dtoResult;
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
