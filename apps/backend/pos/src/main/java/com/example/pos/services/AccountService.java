/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.services;

import com.example.pos.entities.Account;
import com.example.pos.repositories.AccountRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
