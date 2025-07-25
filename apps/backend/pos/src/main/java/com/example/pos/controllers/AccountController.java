/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.controllers;

import com.example.pos.dto.AccountDTO;
import com.example.pos.dto.AccountEntryForm;
import com.example.pos.dto.ApiError;
import com.example.pos.dto.BaseResponse;
import com.example.pos.dto.Paging;
import com.example.pos.dto.PagingResult;
import com.example.pos.entities.Account;
import com.example.pos.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Management", description = "APIs for managing accounts")
public class AccountController {

    private final AccountService accountService;
    private static final List<String> VALID_SORT_COLUMNS = List.of("id", "code", "name", "level", "type", "category", "currency", "createdAt", "updatedAt");

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Get all accounts", description = "Retrieves a list of all accounts in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all accounts"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    @GetMapping
    public List<AccountDTO> getAllAccounts() {
        return accountService.getAllAccounts().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Operation(summary = "Search accounts", description = "Search accounts with pagination and sorting")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved accounts"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    @GetMapping("/search")    
    public PagingResult<AccountDTO> searchAccounts(
            @Parameter(description = "Search filter text") @RequestParam(required = false) String filter,
            @Parameter(description = "Page number (1-based)") @RequestParam(required = false, defaultValue = "1") Integer page,
            @Parameter(description = "Number of items per page") @RequestParam(required = false, defaultValue = "100") Integer pageSize,
            @Parameter(description = "Sort column (id, code, name, level, type, category, currency, createdAt, updatedAt)") @RequestParam(required = false) String sortCol,
            @Parameter(description = "Sort direction (asc or desc)") @RequestParam(required = false, defaultValue = "asc") String sortDir) {
        
        Paging paging = new Paging();
        // Normalize filter value - trim whitespace and convert empty string to null
        paging.setFilter(filter != null ? filter.trim() : null);
        paging.setPage(page);
        paging.setPageSize(pageSize);
        
        // Only set sort if both column and direction are provided and valid
        if (sortCol != null && !sortCol.isEmpty() && sortDir != null && !sortDir.isEmpty()) {
            // Convert sort column to lowercase to match entity field names
            String normalizedSortCol = sortCol.toLowerCase();
            if (VALID_SORT_COLUMNS.contains(normalizedSortCol)) {
                paging.setSortCol(normalizedSortCol);
                paging.setSortDir(sortDir.toLowerCase());
            }
        }
        
        paging.setValidCols(VALID_SORT_COLUMNS);
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
    
    @GetMapping("/search-cash-accounts")    
    public PagingResult<AccountDTO> searchCashAccounts(            
            @Parameter(description = "Search filter text") @RequestParam(required = false) String filter,
            @Parameter(description = "Page number (1-based)") @RequestParam(required = false, defaultValue = "1") Integer page,
            @Parameter(description = "Number of items per page") @RequestParam(required = false, defaultValue = "100") Integer pageSize,
            @Parameter(description = "Sort column (id, code, name, level, type, category, currency, createdAt, updatedAt)") @RequestParam(required = false) String sortCol,
            @Parameter(description = "Sort direction (asc or desc)") @RequestParam(required = false, defaultValue = "asc") String sortDir) {
        
        Paging paging = new Paging();
        // Normalize filter value - trim whitespace and convert empty string to null
        paging.setFilter(filter != null ? filter.trim() : null);
        paging.setPage(page);
        paging.setPageSize(pageSize);
        
        // Only set sort if both column and direction are provided and valid
        if (sortCol != null && !sortCol.isEmpty() && sortDir != null && !sortDir.isEmpty()) {
            // Convert sort column to lowercase to match entity field names
            String normalizedSortCol = sortCol.toLowerCase();
            if (VALID_SORT_COLUMNS.contains(normalizedSortCol)) {
                paging.setSortCol(normalizedSortCol);
                paging.setSortDir(sortDir.toLowerCase());
            }
        }
        
        paging.setValidCols(VALID_SORT_COLUMNS);
        paging.validateSort();
        paging.init(); // Ensure calculation is done
        
        PagingResult<Account> result = accountService.searchCashAccounts(paging);
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
    
    @GetMapping("/search-inventory-accounts")    
    public PagingResult<AccountDTO> searchInventoryAccounts(            
            @Parameter(description = "Search filter text") @RequestParam(required = false) String filter,
            @Parameter(description = "Page number (1-based)") @RequestParam(required = false, defaultValue = "1") Integer page,
            @Parameter(description = "Number of items per page") @RequestParam(required = false, defaultValue = "100") Integer pageSize,
            @Parameter(description = "Sort column (id, code, name, level, type, category, currency, createdAt, updatedAt)") @RequestParam(required = false) String sortCol,
            @Parameter(description = "Sort direction (asc or desc)") @RequestParam(required = false, defaultValue = "asc") String sortDir) {
        
        Paging paging = new Paging();
        // Normalize filter value - trim whitespace and convert empty string to null
        paging.setFilter(filter != null ? filter.trim() : null);
        paging.setPage(page);
        paging.setPageSize(pageSize);
        
        // Only set sort if both column and direction are provided and valid
        if (sortCol != null && !sortCol.isEmpty() && sortDir != null && !sortDir.isEmpty()) {
            // Convert sort column to lowercase to match entity field names
            String normalizedSortCol = sortCol.toLowerCase();
            if (VALID_SORT_COLUMNS.contains(normalizedSortCol)) {
                paging.setSortCol(normalizedSortCol);
                paging.setSortDir(sortDir.toLowerCase());
            }
        }
        
        paging.setValidCols(VALID_SORT_COLUMNS);
        paging.validateSort();
        paging.init(); // Ensure calculation is done
        
        PagingResult<Account> result = accountService.searchInventoryAccounts(paging);
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

    @GetMapping("/search-income-accounts")    
    public PagingResult<AccountDTO> searchIncomeAccounts(            
            @Parameter(description = "Search filter text") @RequestParam(required = false) String filter,
            @Parameter(description = "Page number (1-based)") @RequestParam(required = false, defaultValue = "1") Integer page,
            @Parameter(description = "Number of items per page") @RequestParam(required = false, defaultValue = "100") Integer pageSize,
            @Parameter(description = "Sort column (id, code, name, level, type, category, currency, createdAt, updatedAt)") @RequestParam(required = false) String sortCol,
            @Parameter(description = "Sort direction (asc or desc)") @RequestParam(required = false, defaultValue = "asc") String sortDir) {
        
        Paging paging = new Paging();
        // Normalize filter value - trim whitespace and convert empty string to null
        paging.setFilter(filter != null ? filter.trim() : null);
        paging.setPage(page);
        paging.setPageSize(pageSize);
        
        // Only set sort if both column and direction are provided and valid
        if (sortCol != null && !sortCol.isEmpty() && sortDir != null && !sortDir.isEmpty()) {
            // Convert sort column to lowercase to match entity field names
            String normalizedSortCol = sortCol.toLowerCase();
            if (VALID_SORT_COLUMNS.contains(normalizedSortCol)) {
                paging.setSortCol(normalizedSortCol);
                paging.setSortDir(sortDir.toLowerCase());
            }
        }
        
        paging.setValidCols(VALID_SORT_COLUMNS);
        paging.validateSort();
        paging.init(); // Ensure calculation is done
        
        PagingResult<Account> result = accountService.searchIncomeAccounts(paging);
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
    
    @GetMapping("/search-expense-accounts")    
    public PagingResult<AccountDTO> searchExpenseAccounts(            
            @Parameter(description = "Search filter text") @RequestParam(required = false) String filter,
            @Parameter(description = "Page number (1-based)") @RequestParam(required = false, defaultValue = "1") Integer page,
            @Parameter(description = "Number of items per page") @RequestParam(required = false, defaultValue = "100") Integer pageSize,
            @Parameter(description = "Sort column (id, code, name, level, type, category, currency, createdAt, updatedAt)") @RequestParam(required = false) String sortCol,
            @Parameter(description = "Sort direction (asc or desc)") @RequestParam(required = false, defaultValue = "asc") String sortDir) {
        
        Paging paging = new Paging();
        // Normalize filter value - trim whitespace and convert empty string to null
        paging.setFilter(filter != null ? filter.trim() : null);
        paging.setPage(page);
        paging.setPageSize(pageSize);
        
        // Only set sort if both column and direction are provided and valid
        if (sortCol != null && !sortCol.isEmpty() && sortDir != null && !sortDir.isEmpty()) {
            // Convert sort column to lowercase to match entity field names
            String normalizedSortCol = sortCol.toLowerCase();
            if (VALID_SORT_COLUMNS.contains(normalizedSortCol)) {
                paging.setSortCol(normalizedSortCol);
                paging.setSortDir(sortDir.toLowerCase());
            }
        }
        
        paging.setValidCols(VALID_SORT_COLUMNS);
        paging.validateSort();
        paging.init(); // Ensure calculation is done
        
        PagingResult<Account> result = accountService.searchExpenseAccounts(paging);
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
    
    @PostMapping
    public ResponseEntity<? extends Object> createAccount(@Valid @RequestBody AccountEntryForm form){
        var data = accountService.createAccount(form);
        BaseResponse response = new BaseResponse();
        response.setMessage("Success");
        response.setStatus("Success");
        //response.setTime(TimeHelper.getCurrentTimeYYYYMMDDHHmmss());
        response.setData(data);
        return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
        
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<? extends Object> updateAccount(@PathVariable long id, @Valid @RequestBody AccountEntryForm form){        
        var data = accountService.updateAccount(id, form);
        BaseResponse response = new BaseResponse();
        response.setMessage("Success");
        response.setStatus("Success");
        //response.setTime(TimeHelper.getCurrentTimeYYYYMMDDHHmmss());
        response.setData(data);
        return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
    }
}
