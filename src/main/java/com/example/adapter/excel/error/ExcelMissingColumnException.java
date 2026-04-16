package com.example.adapter.excel.error;

public class ExcelMissingColumnException extends ExcelSchemaException {
    public ExcelMissingColumnException(String sheetName, String columnName) {
        super("EXCEL_MISSING_COLUMN",
                "Missing required column '" + columnName + "' in sheet '" + sheetName + "'",
                new ExcelLocation(sheetName, 0, null),
                null);
    }
}
