package com.example.adapter.excel.error;

public abstract class ExcelCellException extends ExcelMappingException {
    private final String columnName;

    protected ExcelCellException(String errorCode, String message, ExcelLocation location, String columnName, Throwable cause) {
        super(errorCode, message, location, cause);
        this.columnName = columnName;
    }

    public String columnName() {
        return columnName;
    }
}
