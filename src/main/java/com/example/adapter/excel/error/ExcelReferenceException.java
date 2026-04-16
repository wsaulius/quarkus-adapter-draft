package com.example.adapter.excel.error;

public abstract class ExcelReferenceException extends ExcelMappingException {
    protected ExcelReferenceException(String errorCode, String message, ExcelLocation location, Throwable cause) {
        super(errorCode, message, location, cause);
    }
}
