/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.services;

import com.example.pos.dto.AccountEntryForm;
import com.example.pos.dto.Paging;
import com.example.pos.dto.PagingResult;
import com.example.pos.entities.Account;
import com.example.pos.entities.AccountCategory;
import com.example.pos.entities.AccountType;
import com.example.pos.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    
    public PagingResult<Account> searchCashAccounts(Paging paging) {
        var parentCashAccount = repository.findByCode("1-1100");
        if(parentCashAccount.isPresent()){
            return this.searchAccountsByParentID(parentCashAccount.get().getId(), paging);
        }
        PagingResult<Account> result = new PagingResult<>();
        return result;
    }
    
    public PagingResult<Account> searchInventoryAccounts(Paging paging) {
        var parentCashAccount = repository.findByCode("1-2000");
        if(parentCashAccount.isPresent()){
            return this.searchAccountsByParentID(parentCashAccount.get().getId(), paging);
        }
        PagingResult<Account> result = new PagingResult<>();
        return result;
    }
    
    public PagingResult<Account> searchIncomeAccounts(Paging paging) {
        var parentCashAccount = repository.findByCode("4-0000");
        if(parentCashAccount.isPresent()){
            return repository.searchRecursiveAccountsByParent(parentCashAccount.get(), paging);
        }
        PagingResult<Account> result = new PagingResult<>();
        return result;
    }
    
    public PagingResult<Account> searchExpenseAccounts(Paging paging) {
        var parentCashAccount = repository.findByCode("6-0000");
        if(parentCashAccount.isPresent()){
            return repository.searchRecursiveAccountsByParent(parentCashAccount.get(), paging);
        }
        PagingResult<Account> result = new PagingResult<>();
        return result;
    }
    
    public PagingResult<Account> searchAccountsByParentID(long parentID, Paging paging) {
        return repository.searchAccountsByParentID(parentID, paging);
    }
    
    public Account createAccount(AccountEntryForm form){
        var parentCashAccount = repository.findByCode(form.getParentAccount());
        if(!parentCashAccount.isPresent()){
            throw new ServiceException("Parent code not found", 0);
        }
        
        Account account = new Account();
        account.setActive(true);
        account.setCashBank(form.isIsCashBank());
        
        AccountCategory category = AccountCategory.valueOf(form.getAccountCategory());
        account.setCategory(category);
        
        account.setCode(form.getCode());
        //account.setCurrency(currency);
        account.setName(form.getName());
        account.setParent(parentCashAccount.get());
        
        AccountType type = AccountType.valueOf(form.getAccountType());
        account.setType(type);
        return repository.save(account);
    }
    
    
    public Optional<Account> find(long id){
        return this.repository.findById(id);
    }
    
    public Account updateAccount(long id, AccountEntryForm form){
        var parentCashAccount = repository.findByCode(form.getParentAccount());
        if(!parentCashAccount.isPresent()){
            throw new ServiceException("Parent code not found", 0);
        }
        var existing = find(id);
        if(!existing.isPresent()){
            throw new ServiceException("item not found", 0);
        }
        Account account = existing.get();
        
        account.setCashBank(form.isIsCashBank());
        
        AccountCategory category = AccountCategory.valueOf(form.getAccountCategory());
        account.setCategory(category);
        
        account.setCode(form.getCode());
        //account.setCurrency(currency);
        account.setName(form.getName());
        account.setParent(parentCashAccount.get());
        
        AccountType type = AccountType.valueOf(form.getAccountType());
        account.setType(type);
        return repository.save(account);
    }
    
    public boolean deleteAccount(long id){
        var existing = find(id);
        if(!existing.isPresent()){
            throw new ServiceException("item not found", 0);
        }
        var parent = repository.findByParentId(id);
        if(!parent.isEmpty()){
            throw new ServiceException("item is in use", 0);
        }
        repository.delete(existing.get());
        return true;
    }
            
}
