package com.example.adapter.excel.error;
public abstract class ExcelMappingException extends RuntimeException {
    private final ExcelLocation location;
    private final String code;
    protected ExcelMappingException(String code, String message, ExcelLocation location, Throwable cause) {
        super(message, cause); this.code = code; this.location = location;
    }
    public ExcelLocation location() { return location; }
    public String code() { return code; }
}
