package com.example.adapter.excel.error;
public class ExcelSheetNotFoundException extends ExcelMappingException {
    public ExcelSheetNotFoundException(String sheet) { super("EXCEL_SHEET_NOT_FOUND", "Sheet not found: '" + sheet + "'", new ExcelLocation(sheet, null, null), null); }
}
