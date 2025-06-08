/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.seeder;

import com.example.pos.entities.Account;
import com.example.pos.entities.AccountCategory;
import com.example.pos.entities.AccountType;
import com.example.pos.repositories.AccountRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *
 * @author araneta
 */
@Component
public class DataSeeder implements CommandLineRunner {
    private final AccountRepository accountRepository;

    public DataSeeder(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    @Override
    public void run(String... args) {
        if (accountRepository.count() == 0) {
            List<Account> accounts = Arrays.asList(
                new Account("1-10001", "Kas Kecil", AccountType.DETAIL, AccountCategory.CURRENT_ASSET, null, new BigDecimal("500000.00")),
                new Account("1-10002", "Kas Besar", AccountType.DETAIL, AccountCategory.CURRENT_ASSET, null, new BigDecimal("3000000.00")),
                new Account("1-10003", "Bank BCA", AccountType.DETAIL, AccountCategory.CURRENT_ASSET, null, new BigDecimal("10000000.00")),
                new Account("1-10004", "Bank Mandiri", AccountType.DETAIL, AccountCategory.CURRENT_ASSET, null, new BigDecimal("8000000.00")),
                new Account("2-20001", "Utang Dagang", AccountType.DETAIL, AccountCategory.SHORT_TERM_LIABILITY, null, BigDecimal.ZERO),
                new Account("2-20002", "Utang Bank", AccountType.DETAIL, AccountCategory.SHORT_TERM_LIABILITY, null, BigDecimal.ZERO),
                new Account("3-30001", "Modal Pemilik", AccountType.DETAIL, AccountCategory.EQUITY, null, new BigDecimal("20000000.00")),
                new Account("4-40001", "Pendapatan Penjualan", AccountType.DETAIL, AccountCategory.OPERATIONAL_INCOME, null, BigDecimal.ZERO),
                new Account("5-50001", "Biaya Gaji", AccountType.DETAIL, AccountCategory.OPERATIONAL_EXPENSE, null, BigDecimal.ZERO),
                new Account("5-50002", "Biaya Sewa", AccountType.DETAIL, AccountCategory.OPERATIONAL_EXPENSE, null, BigDecimal.ZERO)
            );

            accountRepository.saveAll(accounts);
            System.out.println("✅ Seeded account data.");
        }
    }
}
