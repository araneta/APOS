package com.example.pos.repositories;

import com.example.pos.dto.Paging;
import com.example.pos.dto.PagingResult;
import com.example.pos.entities.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class AccountRepositoryImpl implements AccountRepositoryCustom {

    private final EntityManager entityManager;

    public AccountRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public PagingResult<Account> searchAccounts(Paging paging) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> query = cb.createQuery(Account.class);
        Root<Account> root = query.from(Account.class);

        List<Predicate> predicates = new ArrayList<>();
        
        if (paging.getFilter() != null && !paging.getFilter().trim().isEmpty()) {
            String likeFilter = "%" + paging.getFilter().toLowerCase() + "%";
            predicates.add(cb.or(
                cb.like(cb.lower(root.get("code")), likeFilter),
                cb.like(cb.lower(root.get("name")), likeFilter)
            ));
        }
        
        query.where(predicates.toArray(new Predicate[0]));
        
        // Apply sorting
        if (!paging.getSort().isEmpty()) {
            List<Order> orders = new ArrayList<>();
            for (Map.Entry<String, String> entry : paging.getSort().entrySet()) {
                if ("asc".equalsIgnoreCase(entry.getValue())) {
                    orders.add(cb.asc(root.get(entry.getKey())));
                } else {
                    orders.add(cb.desc(root.get(entry.getKey())));
                }
            }
            query.orderBy(orders);
        } else {
            query.orderBy(cb.asc(root.get("code")));
        }

        // Get total count
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Account> countRoot = countQuery.from(Account.class);
        countQuery.select(cb.count(countRoot));
        countQuery.where(predicates.toArray(new Predicate[0]));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        // Get paginated results
        List<Account> accounts = entityManager.createQuery(query)
            .setFirstResult(paging.getStart())
            .setMaxResults(paging.getPageSize())
            .getResultList();

        // Create PagingResult
        PagingResult<Account> result = new PagingResult<>();
        result.setData(accounts);
        result.setTotalRecords(total.intValue());
        result.calculate(paging);
        
        return result;
    }
} 