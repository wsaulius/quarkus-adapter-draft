package com.example.adapter.excel.error;

public class ExcelSheetNotFoundException extends ExcelAccessException {
    public ExcelSheetNotFoundException(String sheetName) {
        super("EXCEL_SHEET_NOT_FOUND",
                "Sheet not found: '" + sheetName + "'",
                new ExcelLocation(sheetName, null, null),
                null);
    }
}
