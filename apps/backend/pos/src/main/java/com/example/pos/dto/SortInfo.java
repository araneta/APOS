/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.dto;

/**
 *
 * @author araneta
 */
public class SortInfo {
    private String sortCol;
        private String sortDir;

        public SortInfo() {}

        public SortInfo(String sortCol, String sortDir) {
            this.sortCol = sortCol;
            this.sortDir = sortDir;
        }

        public String getSortCol() {
            return sortCol;
        }

        public void setSortCol(String sortCol) {
            this.sortCol = sortCol;
        }

        public String getSortDir() {
            return sortDir;
        }

        public void setSortDir(String sortDir) {
            this.sortDir = sortDir;
        }
}
