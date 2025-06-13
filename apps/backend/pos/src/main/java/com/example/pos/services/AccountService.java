/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.services;

import com.example.pos.dto.Paging;
import com.example.pos.dto.PagingResult;
import com.example.pos.entities.Account;
import com.example.pos.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author araneta
 */
@Service
public class AccountService {
    private final AccountRepository repository;
    
    @Autowired
    public AccountService(AccountRepository repository){
        this.repository = repository;
    }
    
    public List<Account> getAllAccounts(){
        return this.repository.findAll();
    }

    public PagingResult<Account> searchAccounts(Paging paging) {
        return repository.searchAccounts(paging);
    }
}
