/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.dto;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Paging {
    private String filter;
    private Integer page = 1;
    private Integer pageSize = 100;
    private Integer start;
    private Integer end;
    private Map<String, String> sort = new LinkedHashMap<>();
    private List<String> validCols = new ArrayList<>();
    private List<SortInfo> newsorts2 = new ArrayList<>();

    @PostConstruct
    public void init() {
        calculate();
    }

    public void setPage(Integer page) {
        this.page = (page != null && page > 0) ? page : 1;
        calculate();
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = (pageSize != null && pageSize > 0) ? pageSize : 100;
        calculate();
    }

    public void setSortCol(String col) {
        if (col != null && !col.isEmpty()) {
            this.sort.put(col, "asc");
        }
    }

    public void setSortDir(String dir) {
        if (!this.sort.isEmpty()) {
            String lastKey = new ArrayList<>(this.sort.keySet()).get(this.sort.size() - 1);
            this.sort.put(lastKey, dir != null ? dir : "asc");
        }
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setValidCols(List<String> cols) {
        this.validCols = cols;
    }

    public void validateSort() {
        if (!validCols.isEmpty() && !sort.isEmpty()) {
            Map<String, String> validatedSort = new LinkedHashMap<>();
            List<SortInfo> validatedList = new ArrayList<>();

            for (String valid : validCols) {
                for (Map.Entry<String, String> entry : sort.entrySet()) {
                    if (valid.equals(entry.getKey())) {
                        validatedSort.put(entry.getKey(), entry.getValue());
                        validatedList.add(new SortInfo(entry.getKey(), entry.getValue()));
                    }
                }
            }

            this.sort = validatedSort;
            this.newsorts2 = validatedList;
        }
    }

    private void calculate() {
        this.start = (page - 1) * pageSize;
        this.end = start + pageSize;
    }

    // --- Getters ---
    public Integer getPage() { return page; }
    public Integer getPageSize() { return pageSize; }
    public Integer getStart() { return start; }
    public Integer getEnd() { return end; }
    public String getFilter() { return filter; }
    public Map<String, String> getSort() { return sort; }
    public List<SortInfo> getSortArray() { return newsorts2; }

}
