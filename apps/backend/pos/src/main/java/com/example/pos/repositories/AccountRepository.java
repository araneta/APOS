/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.repositories;

/**
 *
 * @author araneta
 */
import com.example.pos.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByCode(String code);

    List<Account> findByIsActiveTrue();

    List<Account> findByParentId(Long parentId);

    List<Account> findByIsCashBankTrue();

    List<Account> findByCurrency(String currency);

    List<Account> findByCategory(String category);
}