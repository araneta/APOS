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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

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
        List<Order> orders = new ArrayList<>();
        if (!paging.getSort().isEmpty()) {
            for (Map.Entry<String, String> entry : paging.getSort().entrySet()) {
                if ("asc".equalsIgnoreCase(entry.getValue())) {
                    orders.add(cb.asc(root.get(entry.getKey())));
                } else {
                    orders.add(cb.desc(root.get(entry.getKey())));
                }
            }
        }
        // Add default sorting by code if no sort is specified
        if (orders.isEmpty()) {
            orders.add(cb.asc(root.get("code")));
        }
        query.orderBy(orders);

        // Get total count using a separate query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Account> countRoot = countQuery.from(Account.class);
        countQuery.select(cb.count(countRoot));
        
        // Apply the same predicates to the count query
        List<Predicate> countPredicates = new ArrayList<>();
        if (paging.getFilter() != null && !paging.getFilter().trim().isEmpty()) {
            String likeFilter = "%" + paging.getFilter().toLowerCase() + "%";
            countPredicates.add(cb.or(
                cb.like(cb.lower(countRoot.get("code")), likeFilter),
                cb.like(cb.lower(countRoot.get("name")), likeFilter)
            ));
        }
        countQuery.where(countPredicates.toArray(new Predicate[0]));
        
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
    
    @Override
    public PagingResult<Account> searchAccountsByParentID(long parentID, Paging paging){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> query = cb.createQuery(Account.class);
        Root<Account> root = query.from(Account.class);

        List<Predicate> predicates = new ArrayList<>();
        
        predicates.add(cb.equal(root.get("parent").get("id"), parentID));
        
        if (paging.getFilter() != null && !paging.getFilter().trim().isEmpty()) {
            String likeFilter = "%" + paging.getFilter().toLowerCase() + "%";
            predicates.add(cb.or(
                cb.like(cb.lower(root.get("code")), likeFilter),
                cb.like(cb.lower(root.get("name")), likeFilter)
            ));
        }
        
        query.where(predicates.toArray(new Predicate[0]));
        
        // Apply sorting
        List<Order> orders = new ArrayList<>();
        if (!paging.getSort().isEmpty()) {
            for (Map.Entry<String, String> entry : paging.getSort().entrySet()) {
                if ("asc".equalsIgnoreCase(entry.getValue())) {
                    orders.add(cb.asc(root.get(entry.getKey())));
                } else {
                    orders.add(cb.desc(root.get(entry.getKey())));
                }
            }
        }
        // Add default sorting by code if no sort is specified
        if (orders.isEmpty()) {
            orders.add(cb.asc(root.get("code")));
        }
        query.orderBy(orders);

        // Get total count using a separate query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Account> countRoot = countQuery.from(Account.class);
        countQuery.select(cb.count(countRoot));
        
        // Apply the same predicates to the count query
        List<Predicate> countPredicates = new ArrayList<>();
        countPredicates.add(cb.equal(countRoot.get("parent").get("id"), parentID));
        if (paging.getFilter() != null && !paging.getFilter().trim().isEmpty()) {
            String likeFilter = "%" + paging.getFilter().toLowerCase() + "%";
            countPredicates.add(cb.or(
                cb.like(cb.lower(countRoot.get("code")), likeFilter),
                cb.like(cb.lower(countRoot.get("name")), likeFilter)
            ));
        }
        countQuery.where(countPredicates.toArray(new Predicate[0]));
        
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
    
    
    @Override
    public PagingResult<Account> searchRecursiveAccountsByParent(Account parent, Paging paging){
        // To collect all matched accounts from all levels
        List<Account> allMatchedAccounts = new ArrayList<>();
        long parentID = parent.getId();
        //also add the parent
        allMatchedAccounts.add(parent);
        
        Set<Long> visitedParentIds = new HashSet<>();
        Queue<Long> queue = new LinkedList<>();
        queue.add(parentID);

        // Filter and sort variables
        String likeFilter = null;
        if (paging.getFilter() != null && !paging.getFilter().trim().isEmpty()) {
            likeFilter = "%" + paging.getFilter().toLowerCase() + "%";
        }

        // Recursive search using BFS-like queue
        while (!queue.isEmpty()) {
            Long currentParentId = queue.poll();
            if (visitedParentIds.contains(currentParentId)) continue;
            visitedParentIds.add(currentParentId);

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Account> query = cb.createQuery(Account.class);
            Root<Account> root = query.from(Account.class);

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("parent").get("id"), currentParentId));

            if (likeFilter != null) {
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("code")), likeFilter),
                    cb.like(cb.lower(root.get("name")), likeFilter)
                ));
            }

            query.where(predicates.toArray(new Predicate[0]));

            // Apply sorting
            List<Order> orders = new ArrayList<>();
            if (!paging.getSort().isEmpty()) {
                for (Map.Entry<String, String> entry : paging.getSort().entrySet()) {
                    if ("asc".equalsIgnoreCase(entry.getValue())) {
                        orders.add(cb.asc(root.get(entry.getKey())));
                    } else {
                        orders.add(cb.desc(root.get(entry.getKey())));
                    }
                }
            }
            if (orders.isEmpty()) {
                orders.add(cb.asc(root.get("code")));
            }
            query.orderBy(orders);

            List<Account> children = entityManager.createQuery(query).getResultList();

            allMatchedAccounts.addAll(children);

            // Add child IDs to queue for next level search
            for (Account account : children) {
                queue.add(account.getId());
            }
        }

        // Manual pagination
        int total = allMatchedAccounts.size();
        int fromIndex = paging.getStart();
        int toIndex = Math.min(fromIndex + paging.getPageSize(), total);

        List<Account> pagedAccounts = total > 0 && fromIndex < total
            ? allMatchedAccounts.subList(fromIndex, toIndex)
            : Collections.emptyList();

        // Prepare result
        PagingResult<Account> result = new PagingResult<>();
        result.setData(pagedAccounts);
        result.setTotalRecords(total);
        result.calculate(paging);
        return result;
    }

} 