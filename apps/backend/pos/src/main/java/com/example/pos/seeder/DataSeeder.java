/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.seeder;

import com.example.pos.entities.Account;
import com.example.pos.entities.AccountCategory;
import com.example.pos.entities.AccountType;
import com.example.pos.repositories.AccountRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
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
    private AccountCategory mapKelompokToCategory(String kelompok, String namaacc) {
        switch (kelompok) {
            case "1":
                if (namaacc.toUpperCase().contains("TETAP")) return AccountCategory.FIXED_ASSET;
                if (namaacc.toUpperCase().contains("PERSEDIAAN")) return AccountCategory.INVENTORY;
                return AccountCategory.CURRENT_ASSET;

            case "2":
                return AccountCategory.SHORT_TERM_LIABILITY;

            case "3":
                return AccountCategory.EQUITY;

            case "4":
                return AccountCategory.OPERATING_INCOME;

            case "5":
                return AccountCategory.COST_OF_SALES;

            case "6":
                return AccountCategory.OPERATING_EXPENSE;

            case "7":
                return AccountCategory.OTHER_INCOME;

            case "8":
                return AccountCategory.OTHER_EXPENSE;

            default:
                return null;
        }
    }


    @Override
    public void run(String... args) throws Exception {
        if (accountRepository.count() == 0) {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = new ClassPathResource("cashofaccount.json").getInputStream();

            JsonNode root = mapper.readTree(inputStream).get("tbl_perkiraan");

            Map<String, Account> tempAccountMap = new HashMap<>();
            List<Account> allAccounts = new ArrayList<>();

            // Step 1: Create all accounts (no parent yet)
            for (JsonNode node : root) {
                String code = node.get("kodeacc").asText();
                String name = node.get("namaacc").asText();
                String kelompok = node.get("kelompok").asText();
                String tipeStr = node.get("tipe").asText();
                String currency = node.hasNonNull("matauang") ? node.get("matauang").asText() : null;
                boolean isCashBank = node.get("kasbank").asBoolean();
                boolean isFxDefault = node.get("defmuutm").asBoolean();

                AccountType type = tipeStr.equalsIgnoreCase("H") ? AccountType.HEADER : AccountType.DETAIL;
                AccountCategory category = mapKelompokToCategory(kelompok, name);

                Account account = new Account();
                account.setCode(code);
                account.setName(name);
                account.setType(type);
                account.setCategory(category);
                account.setCurrency(currency);
                account.setCashBank(isCashBank);
                account.setFxDefault(isFxDefault);
                account.setActive(true);

                tempAccountMap.put(code, account);
                allAccounts.add(account);
            }

            // Step 2: Set parent relationships
            for (JsonNode node : root) {
                String code = node.get("kodeacc").asText();
                JsonNode parentNode = node.get("parentacc");

                if (parentNode != null && !parentNode.isNull()) {
                    String parentCode = parentNode.asText();
                    Account child = tempAccountMap.get(code);
                    Account parent = tempAccountMap.get(parentCode);

                    if (parent != null) {
                        child.setParent(parent);
                        child.setLevel(parent.getLevel() + 1);
                    }
                } else {
                    // Root account
                    tempAccountMap.get(code).setLevel(0);
                }
            }

            // Step 3: Save all to DB
            accountRepository.saveAll(allAccounts);
            System.out.println("✅ Seeded account data with parent-child relationships.");
        }
    }

}
