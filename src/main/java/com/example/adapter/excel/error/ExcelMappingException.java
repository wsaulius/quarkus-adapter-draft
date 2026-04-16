package com.example.adapter.excel.error;

public abstract class ExcelMappingException extends RuntimeException {
    private final ExcelLocation location;
    private final String errorCode;

    protected ExcelMappingException(String errorCode, String message, ExcelLocation location, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.location = location;
    }

    public ExcelLocation location() {
        return location;
    }

    public String errorCode() {
        return errorCode;
    }
}
