package com.example.adapter.excel.error;

public abstract class ExcelAccessException extends ExcelMappingException {
    protected ExcelAccessException(String errorCode, String message, ExcelLocation location, Throwable cause) {
        super(errorCode, message, location, cause);
    }
}
