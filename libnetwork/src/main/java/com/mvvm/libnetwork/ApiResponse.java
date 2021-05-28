package com.mvvm.libnetwork;

public class ApiResponse<T> {
    public boolean success;
    public int status;
    public String message;
    public T body;
}
