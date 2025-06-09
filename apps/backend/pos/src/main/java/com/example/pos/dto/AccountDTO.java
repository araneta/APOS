package com.example.pos.dto;

import com.example.pos.entities.AccountCategory;
import com.example.pos.entities.AccountType;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AccountDTO {
    private Long id;
    private String code;
    private String name;
    private Integer level;
    private AccountType type;
    private AccountCategory category;
    private boolean cashBank;
    private boolean fxDefault;
    private String currency;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AccountDTO parent;
} 