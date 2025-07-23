/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.services;

/**
 *
 * @author araneta
 */
public class ServiceException extends RuntimeException {
    private final int errorCode;

    public ServiceException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceException(String message, int errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
