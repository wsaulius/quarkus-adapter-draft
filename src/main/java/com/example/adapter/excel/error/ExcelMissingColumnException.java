package com.example.adapter.excel.error;
public class ExcelMissingColumnException extends ExcelMappingException {
    public ExcelMissingColumnException(String sheet, String col) { super("EXCEL_MISSING_COLUMN", "Missing required column '" + col + "' in sheet '" + sheet + "'", new ExcelLocation(sheet, 0, null), null); }
}
