/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.dto;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author araneta
 */
@JsonInclude(JsonInclude.Include.NON_NULL)

public class BaseResponse {
    private String status;
    private String message;
    private Object data;
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
