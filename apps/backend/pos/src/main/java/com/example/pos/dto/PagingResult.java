/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.dto;

import java.util.List;

/**
 *
 * @author araneta
 */
public class PagingResult<T> {

    private int totalDisplayRecords = 0;
    private int totalRecords = 0;
    private List<T> data;
    private int start = 0;
    private int end = 0;
    private int page = 1;
    private int totalPages = 0;
    private List<SortInfo> sort;

    public PagingResult() {}

    public PagingResult(int totalDisplayRecords, int totalRecords, List<T> data, int start, int end, int page, int totalPages, List<SortInfo> sort) {
        this.totalDisplayRecords = totalDisplayRecords;
        this.totalRecords = totalRecords;
        this.data = data;
        this.start = start;
        this.end = end;
        this.page = page;
        this.totalPages = totalPages;
        this.sort = sort;
    }

    // Getters and Setters

    public int getTotalDisplayRecords() {
        return totalDisplayRecords;
    }

    public void setTotalDisplayRecords(int totalDisplayRecords) {
        this.totalDisplayRecords = totalDisplayRecords;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
        if (data != null) {
            this.totalDisplayRecords = data.size();
        }
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<SortInfo> getSort() {
        return sort;
    }

    public void setSort(List<SortInfo> sort) {
        this.sort = sort;
    }

    public void calculate(Paging paging) {
        this.start = paging.getStart();
        this.end = this.start + this.totalDisplayRecords;
        this.page = paging.getPage();
        if (paging.getPageSize() > 0) {
            this.totalPages = (int) Math.ceil((double) this.totalRecords / paging.getPageSize());
        } else {
            this.totalPages = 1;
        }
        this.sort = paging.getSortArray();
    }

}
