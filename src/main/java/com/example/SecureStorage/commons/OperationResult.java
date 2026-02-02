package com.example.SecureStorage.commons;

public class OperationResult<T> {
    private T data;
    private String errorMessage;
    private boolean success;

    public OperationResult(T data) {
        this.data = data;
        this.success = true;
    }

    public OperationResult(String errorMessage) {
        this.errorMessage = errorMessage;
        this.success = false;
    }

    public T getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public static <T> OperationResult<T> success(T data) {
        return new OperationResult<>(data);
    }

    public static <T> OperationResult<T> error(String errorMessage) {
        return new OperationResult<>(errorMessage);
    }
}
