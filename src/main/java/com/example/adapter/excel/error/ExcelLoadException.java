package com.example.adapter.excel.error;

public class ExcelLoadException extends ExcelMappingException {
    public ExcelLoadException(String message, Throwable cause) {
        super("EXCEL_LOAD_FAILED", message, new ExcelLocation("<workbook>", null, null), cause);
    }
}
