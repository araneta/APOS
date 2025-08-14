package com.example.pos.repositories;

import com.example.pos.dto.Paging;
import com.example.pos.dto.PagingResult;
import com.example.pos.entities.Account;
import com.example.pos.entities.AccountCategory;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AccountRepositoryCustom {
    PagingResult<Account> searchAccounts(Paging paging);
    
    PagingResult<Account> searchAccountsByParentID(long parentID, Paging paging);
    PagingResult<Account> searchRecursiveAccountsByParent(Account parent, Paging paging);
    List<Account> findAllParentsByAccountCategory(AccountCategory cat);
} 