package com.example.adapter.excel.error;

public abstract class ExcelSchemaException extends ExcelMappingException {
    protected ExcelSchemaException(String errorCode, String message, ExcelLocation location, Throwable cause) {
        super(errorCode, message, location, cause);
    }
}
